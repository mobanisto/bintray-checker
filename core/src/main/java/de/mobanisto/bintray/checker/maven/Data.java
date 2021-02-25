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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import de.topobyte.maven.core.VersionedArtifact;
import de.topobyte.webpaths.WebPath;
import lombok.Getter;

public class Data
{

	@Getter
	private Server bintray = new Server("https://jcenter.bintray.com/");
	@Getter
	private List<Server> servers = new ArrayList<>();
	{
		servers.add(bintray);
		servers.add(new Server("https://dl.google.com/android/maven2/"));
		servers.add(new Server("https://jitpack.io"));
		servers.add(new Server("https://plugins.gradle.org/m2/"));
		servers.add(new Server("https://repo1.maven.org/maven2/"));
	}

	private Map<WebPath, Server> pathToServer = new LinkedHashMap<>();
	private Map<VersionedArtifact, Server> artifactToServer = new LinkedHashMap<>();
	private Multimap<VersionedArtifact, Server> artifactToAlternatives = LinkedHashMultimap
			.create();

	public void setServed(WebPath path, Server server,
			VersionedArtifact artifact)
	{
		pathToServer.put(path, server);
		if (artifact != null && !artifactToServer.containsKey(artifact)) {
			artifactToServer.put(artifact, server);
		}
	}

	public void addAlternative(VersionedArtifact artifact, Server other)
	{
		artifactToAlternatives.put(artifact, other);
	}

	public Set<VersionedArtifact> getArtifacts()
	{
		return artifactToServer.keySet();
	}

	public Server getServer(VersionedArtifact artifact)
	{
		return artifactToServer.get(artifact);
	}

	public Collection<Server> getAlternatives(VersionedArtifact artifact)
	{
		return artifactToAlternatives.get(artifact);
	}

}
