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

package de.mobanisto.changeme.resolving;

import javax.servlet.http.HttpServletRequest;

import de.mobanisto.changeme.pages.markdown.MarkdownResourceGenerator;
import de.mobanisto.changeme.pages.other.AboutGenerator;
import de.mobanisto.changeme.pages.other.IndexGenerator;
import de.topobyte.jsoup.ContentGeneratable;
import de.topobyte.webgun.resolving.pathspec.PathSpec;
import de.topobyte.webgun.resolving.pathspec.PathSpecResolver;
import de.topobyte.webpaths.WebPath;

public class MainPathResolver extends PathSpecResolver<ContentGeneratable, Void>
{

	@Override
	public ContentGeneratable getGenerator(WebPath path,
			HttpServletRequest request, Void data)
	{
		if (path.getNameCount() == 0) {
			return new IndexGenerator(path);
		}
		return super.getGenerator(path, request, data);
	}

	{
		map(new PathSpec("about"), (path, output, request, data) -> {
			return new AboutGenerator(path);
		});
		map(new PathSpec("impressum"), (path, output, request, data) -> {
			return new MarkdownResourceGenerator(path,
					"markdown/de/impressum.md");
		});
		map(new PathSpec("privacy-policy"), (path, output, request, data) -> {
			return new MarkdownResourceGenerator(path,
					"markdown/de/privacy-policy.md");
		});
	}

}
