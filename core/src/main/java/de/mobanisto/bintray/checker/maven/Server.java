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
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with bintray-checker. If not, see <http://www.gnu.org/licenses/>.

package de.mobanisto.bintray.checker.maven;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;
import lombok.Getter;

public class Server
{

	@Getter
	private String scheme;
	@Getter
	private String host;
	@Getter
	private int port;
	@Getter
	private WebPath repoPath;

	@Getter
	private URI baseUri;

	public Server(String uri) throws IllegalArgumentException
	{
		try {
			URI u = new URI(uri);
			scheme = u.getScheme();
			port = u.getPort();
			host = u.getHost();
			repoPath = WebPaths.get(u.getPath());

			baseUri = new URIBuilder().setScheme(scheme).setHost(host)
					.setPort(port).setPath(repoPath.toString()).build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException();
		}
	}

	public URI resolve(String path) throws URISyntaxException
	{
		return resolve(WebPaths.get(path));
	}

	public URI resolve(WebPath relative) throws URISyntaxException
	{
		WebPath resolved = repoPath.resolve(relative);
		return new URIBuilder().setScheme(scheme).setHost(host).setPort(port)
				.setPath(resolved.toString()).build();
	}

}
