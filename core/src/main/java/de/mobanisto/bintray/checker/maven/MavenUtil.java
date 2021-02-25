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

package de.mobanisto.bintray.checker.maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.maven.core.UnversionedArtifact;
import de.topobyte.maven.core.VersionedArtifact;
import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;

// Adapted from topobyte:maven-util
public class MavenUtil
{

	final static Logger logger = LoggerFactory.getLogger(MavenUtil.class);

	public static UnversionedArtifact artifactFromMetadataPath(WebPath file)
	{
		int nc = file.getNameCount();
		if (nc < 3) {
			throw new IllegalArgumentException(
					"the path must have at least 3 components");
		}
		StringBuilder groupId = new StringBuilder();
		groupId.append(file.getName(0));
		for (int i = 1; i < nc - 2; i++) {
			groupId.append(".");
			groupId.append(file.getName(i));
		}
		String artifactId = file.getName(nc - 2).toString();
		return new UnversionedArtifact(groupId.toString(), artifactId);
	}

	public static VersionedArtifact artifactFromPomPath(WebPath file)
	{
		int nc = file.getNameCount();
		if (nc < 4) {
			throw new IllegalArgumentException(
					"the path must have at least 4 components");
		}
		String pomName = file.getName(nc - 1).toString();
		if (!pomName.endsWith(".pom")) {
			throw new IllegalArgumentException(
					"the path does not point to a *.pom file");
		}
		// av has this format: ARTIFACT_ID-VERSION or VERSION
		String av = pomName.substring(0, pomName.length() - 4);

		String artifactId = file.getName(nc - 3).toString();
		String version = file.getName(nc - 2).toString();

		if (!av.equals(artifactId + "-" + version) && !av.equals(version)) {
			throw new IllegalArgumentException("pom file has invalid format");
		}

		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < nc - 3; i++) {
			buffer.append(file.getName(i).toString());
			if (i < nc - 4) {
				buffer.append(".");
			}
		}
		String groupId = buffer.toString();

		return new VersionedArtifact(groupId, artifactId, version);
	}

	public static WebPath toVerbosePath(VersionedArtifact artifact)
	{
		String[] gs = artifact.getGroupId().split("\\.");

		List<String> parts = new ArrayList<>(Arrays.asList(gs));
		parts.add(artifact.getArtifactId());
		parts.add(artifact.getVersion());
		String first = parts.get(0);
		String[] more = parts.subList(1, parts.size())
				.toArray(new String[parts.size() - 1]);

		return WebPaths.get(first, more);
	}

	public static WebPath toVerbosePath(UnversionedArtifact artifact)
	{
		String[] gs = artifact.getGroupId().split("\\.");

		List<String> parts = new ArrayList<>(Arrays.asList(gs));
		parts.add(artifact.getArtifactId());
		String first = parts.get(0);
		String[] more = parts.subList(1, parts.size())
				.toArray(new String[parts.size() - 1]);

		return WebPaths.get(first, more);
	}

	public static WebPath getArtifactPath(VersionedArtifact artifact,
			String extension)
	{
		return getArtifactPath(artifact, null, extension);
	}

	public static WebPath getArtifactPath(VersionedArtifact artifact,
			String classifier, String extension)
	{
		WebPath dir = toVerbosePath(artifact);
		String a = artifact.getArtifactId();
		String v = artifact.getVersion();
		String filename;
		if (classifier == null) {
			filename = String.format("%s-%s.%s", a, v, extension);
		} else {
			filename = String.format("%s-%s-%s.%s", a, v, classifier,
					extension);
		}
		return dir.resolve(filename);
	}

	public static WebPath getPomPath(VersionedArtifact artifact)
	{
		WebPath dir = toVerbosePath(artifact);
		return dir.resolve(PomNames.pom(artifact));
	}

	public static WebPath getMetadataPath(UnversionedArtifact artifact)
	{
		WebPath dir = toVerbosePath(artifact);
		return dir.resolve(Maven.FILENAME_METADATA);
	}

}
