// Copyright 2021 Mobanisto UG (haftungsbeschränkt)
//
// This file is part of bintray-checker.
//
// bintray-checker is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// bintray-checker is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with bintray-checker. If not, see <http://www.gnu.org/licenses/>.

package de.mobanisto.changeme;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import de.mobanisto.changeme.maven.Data;
import de.mobanisto.changeme.maven.MavenUtil;
import de.mobanisto.changeme.maven.Server;
import de.mobanisto.changeme.resolving.MainPathResolver;
import de.mobanisto.changeme.util.ServletUtil;
import de.topobyte.jsoup.ContentGeneratable;
import de.topobyte.jsoup.JsoupServletUtil;
import de.topobyte.maven.core.VersionedArtifact;
import de.topobyte.webgun.exceptions.PageNotFoundException;
import de.topobyte.webgun.resolving.PathResolver;
import de.topobyte.webgun.resolving.Redirecter;
import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;

@WebServlet("/*")
public class IndexServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	static List<PathResolver<ContentGeneratable, Void>> resolvers = new ArrayList<>();
	static {
		resolvers.add(new MainPathResolver());
	}

	private CloseableHttpClient client = HttpClientBuilder.create()
			.disableRedirectHandling().build();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
		System.out.println("GET:" + uri);
		WebPath path = WebPaths.get(uri);

		Map<String, String[]> parameters = request.getParameterMap();

		List<Redirecter> redirecters = new ArrayList<>();

		for (Redirecter redirecter : redirecters) {
			String location = redirecter.redirect(path, parameters);
			if (location != null) {
				response.sendRedirect(location);
				return;
			}
		}

		ContentGeneratable generator = null;

		for (PathResolver<ContentGeneratable, Void> resolver : resolvers) {
			generator = resolver.getGenerator(path, request, null);
			if (generator != null) {
				break;
			}
		}

		if (generator != null) {
			try {
				JsoupServletUtil.respond(response, generator);
				return;
			} catch (PageNotFoundException e) {
				// go on
			}
		}

		boolean served = tryServe(path, response);
		if (served) {
			return;
		}

		ServletUtil.respond404(path, response, null);
	}

	private boolean tryServe(WebPath path, HttpServletResponse response)
	{
		Data data = Website.INSTANCE.getData();
		List<Server> servers = data.getServers();

		VersionedArtifact artifact = null;
		if (path.getNameCount() > 0 && path.getFileName().endsWith(".pom")) {
			artifact = MavenUtil.artifactFromPomPath(path);
			System.out.println("POM: " + artifact);
		}
		for (int i = 0; i < servers.size(); i++) {
			Server server = servers.get(i);
			boolean served = tryServe(server, path, response);
			if (!served) {
				continue;
			}
			data.setServed(path, server, artifact);
			if (server == data.getBintray() && i < servers.size() - 1) {
				// if served from bintray, check the other repos, too
				List<Server> remaining = servers.subList(i + 1, servers.size());
				for (Server other : remaining) {
					boolean possible = checkForExistance(other, path);
					if (possible) {
						data.addAlternative(artifact, other);
					}
				}
			}
		}
		return true;
	}

	private boolean tryServe(Server server, WebPath path,
			HttpServletResponse response)
	{
		try {
			URI target = server.resolve(path);
			System.out.println("TRY: " + target);
			HttpGet get = new HttpGet(target);
			try (CloseableHttpResponse r = client.execute(get)) {
				StatusLine status = r.getStatusLine();
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					InputStream input = r.getEntity().getContent();
					ServletOutputStream output = response.getOutputStream();
					IOUtils.copy(input, output);
					output.close();
					return true;
				}
				return false;
			}
		} catch (URISyntaxException | IOException e) {
			return false;
		}
	}

	private boolean checkForExistance(Server server, WebPath path)
	{
		try {
			URI target = server.resolve(path);
			System.out.println("CHK: " + target);
			HttpGet get = new HttpGet(target);
			try (CloseableHttpResponse r = client.execute(get)) {
				StatusLine status = r.getStatusLine();
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					EntityUtils.consume(r.getEntity());
					System.out.println("→ SUCCESS");
					return true;
				}
				System.out.println("→ FAIL");
				return false;
			}
		} catch (URISyntaxException | IOException e) {
			return false;
		}
	}

}
