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

package de.mobanisto.bintray.checker.pages.other;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.base.Joiner;

import de.mobanisto.bintray.checker.Website;
import de.mobanisto.bintray.checker.maven.Data;
import de.mobanisto.bintray.checker.maven.Server;
import de.mobanisto.bintray.checker.pages.base.SimpleBaseGenerator;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.bootstrap4.components.ListGroupDiv;
import de.topobyte.maven.core.VersionedArtifact;
import de.topobyte.webpaths.WebPath;

public class IndexGenerator extends SimpleBaseGenerator
{

	public IndexGenerator(WebPath path)
	{
		super(path);
	}

	private Data data = Website.INSTANCE.getData();

	@Override
	protected void content()
	{
		content.ac(HTML.h2("Servers"));
		serverList();

		content.ac(HTML.h2("Artifacts"));
		artifacts();

		for (Server server : data.getServers()) {
			artifacts(server);
		}

		content.ac(HTML.h2("Bintray alternatives"));
		bintrayAlternatives();
	}

	private void serverList()
	{
		List<Server> servers = data.getServers();
		ListGroupDiv list = content.ac(Bootstrap.listGroupDiv());
		list.addClass("mb-3");
		for (Server server : servers) {
			String uri = server.getBaseUri().toString();
			list.addA(uri, uri);
		}
	}

	private void replaceEmptyListWithNone(ListGroupDiv list)
	{
		if (list.childNodes().isEmpty()) {
			list.replaceWith(HTML.p().at("none"));
		}
	}

	private void artifacts()
	{
		Set<VersionedArtifact> artifacts = data.getArtifacts();

		ListGroupDiv list = content.ac(Bootstrap.listGroupDiv());
		list.addClass("mb-3");

		for (VersionedArtifact artifact : artifacts) {
			list.addTextItem(artifact.toString());
		}

		replaceEmptyListWithNone(list);
	}

	private void artifacts(Server server)
	{
		content.ac(HTML.h3("Server: " + server.getBaseUri()));
		ListGroupDiv list = content.ac(Bootstrap.listGroupDiv());
		list.addClass("mb-3");

		Set<VersionedArtifact> artifacts = data.getArtifacts();
		for (VersionedArtifact artifact : artifacts) {
			if (server == data.getServer(artifact)) {
				list.addTextItem(artifact.toString());
			}
		}

		replaceEmptyListWithNone(list);
	}

	private void bintrayAlternatives()
	{
		List<VersionedArtifact> atBintray = new ArrayList<>();

		Set<VersionedArtifact> artifacts = data.getArtifacts();
		for (VersionedArtifact artifact : artifacts) {
			if (data.getBintray() == data.getServer(artifact)) {
				atBintray.add(artifact);
			}
		}

		content.ac(HTML.h3("With alternative"));

		ListGroupDiv listWith = content.ac(Bootstrap.listGroupDiv());
		listWith.addClass("mb-3");

		content.ac(HTML.h3("No alternative"));

		ListGroupDiv listWithOut = content.ac(Bootstrap.listGroupDiv());
		listWith.addClass("mb-3");

		for (VersionedArtifact artifact : atBintray) {
			Collection<Server> alternatives = data.getAlternatives(artifact);
			if (alternatives.isEmpty()) {
				listWithOut.addTextItem(artifact.toString());
			} else {
				List<String> names = new ArrayList<>();
				for (Server server : alternatives) {
					names.add(server.getBaseUri().toString());
				}
				listWith.addTextItem(String.format("%s: %s",
						artifact.toString(), Joiner.on(",").join(names)));
			}
		}

		replaceEmptyListWithNone(listWith);
		replaceEmptyListWithNone(listWithOut);
	}

}
