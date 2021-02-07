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

package de.mobanisto.changeme.pages.other;

import de.mobanisto.changeme.Website;
import de.mobanisto.changeme.pages.base.SimpleBaseGenerator;
import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Img;
import de.topobyte.jsoup.components.P;
import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;

public class AboutGenerator extends SimpleBaseGenerator
{

	public AboutGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1(Website.TITLE));

		Div row = content.ac(Bootstrap.row());
		row.addClass("align-items-center");

		Div colLeft = row.ac(HTML.div("col-12 col-sm-4"));
		Div colRight = row.ac(HTML.div("col-12 col-sm-8"));

		Img image = colLeft.ac(HTML.img("/"
				+ WebPaths.get(CacheBusting.resolve("images/mobanisto.svg"))));
		image.addClass("img-fluid");
		image.attr("style", "width: 100%; padding: 15%");

		P p = colRight.ac(HTML.p());
		p.at("About " + Website.TITLE);
		p = colRight.ac(HTML.p());
		p.at("A project by Mo und Seb from ");
		p.ac(HTML.a("https://www.mobanisto.de", "Mobanisto"));
		p.at(".");
	}

}
