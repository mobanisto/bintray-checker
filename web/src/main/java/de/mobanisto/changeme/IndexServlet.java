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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.mobanisto.changeme.resolving.MainPathResolver;
import de.mobanisto.changeme.util.ServletUtil;
import de.topobyte.jsoup.ContentGeneratable;
import de.topobyte.jsoup.JsoupServletUtil;
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

	private interface Responder<T>
	{

		public void respond(WebPath output, HttpServletResponse response,
				T data) throws IOException;

	}

	private <T> void tryGenerate(HttpServletResponse response, WebPath path,
			ContentGeneratable generator, Responder<T> responder, T data)
			throws IOException
	{
		if (generator != null) {
			try {
				JsoupServletUtil.respond(response, generator);
			} catch (PageNotFoundException e) {
				responder.respond(path, response, data);
			}
		} else {
			responder.respond(path, response, data);
		}

	}

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

		tryGenerate(response, path, generator, ServletUtil::respond404,
				(Void) null);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
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

		tryGenerate(response, path, generator, ServletUtil::respond404,
				(Void) null);
	}

}
