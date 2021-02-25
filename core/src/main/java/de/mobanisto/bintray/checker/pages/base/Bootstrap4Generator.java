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

package de.mobanisto.bintray.checker.pages.base;

import static de.topobyte.jsoup.ElementBuilder.script;
import static de.topobyte.jsoup.ElementBuilder.styleSheet;

import java.io.IOException;

import de.mobanisto.bintray.checker.CacheBuster;
import de.mobanisto.bintray.checker.Website;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.Meta;
import de.topobyte.pagegen.core.BaseFileGenerator;
import de.topobyte.pagegen.core.LinkResolver;
import de.topobyte.webpaths.WebPath;

public class Bootstrap4Generator extends BaseFileGenerator
{

	private boolean fluid = false;

	public Bootstrap4Generator(WebPath path, boolean fluid)
	{
		super(path);
		this.fluid = fluid;
	}

	public boolean isFluid()
	{
		return fluid;
	}

	public void setFluid(boolean fluid)
	{
		this.fluid = fluid;
	}

	private static String[] cssPaths = new String[] {
			"bootstrap/css/bootstrap.min.css", "jstree-theme/style.css" };

	private static String[] jsPaths = new String[] { "jquery/jquery.min.js",
			"popper.js/umd/popper.min.js", "bootstrap/js/bootstrap.min.js",
			"jstree/jstree.min.js" };

	public static void setupHeader(LinkResolver resolver, Head head)
	{
		Meta meta = head.ac(HTML.meta());
		meta.attr("http-equiv", "content-type");
		meta.attr("content", "text/html; charset=utf-8");

		meta = head.ac(HTML.meta());
		meta.attr("name", "viewport");
		meta.attr("content",
				"width=device-width, initial-scale=1, shrink-to-fit=no");

		CacheBuster cacheBuster = Website.INSTANCE.getCacheBuster();

		for (String cssPath : cssPaths) {
			head.appendChild(styleSheet(cacheBuster.resolve(cssPath)));
		}

		for (String jsPath : jsPaths) {
			head.appendChild(script(cacheBuster.resolve(jsPath)));
		}
	}

	@Override
	public void generate() throws IOException
	{
		Head head = builder.getHead();

		setupHeader(this, head);

		/*
		 * Main Content
		 */

		if (fluid) {
			content = new Div("container-fluid");
		} else {
			content = new Div("container");
		}

		builder.getBody().appendChild(content);
	}

}
