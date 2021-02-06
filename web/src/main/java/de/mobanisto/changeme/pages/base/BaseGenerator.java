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

package de.mobanisto.changeme.pages.base;

import java.io.IOException;

import org.jsoup.nodes.Element;

import de.mobanisto.changeme.CacheBuster;
import de.mobanisto.changeme.Website;
import de.mobanisto.changeme.widgets.MainFooter;
import de.mobanisto.changeme.widgets.MainMenu;
import de.topobyte.jsoup.ElementBuilder;
import de.topobyte.jsoup.FaviconUtil;
import de.topobyte.jsoup.bootstrap4.components.Menu;
import de.topobyte.jsoup.components.Head;
import de.topobyte.pagegen.bootstrap.Bootstrap4Generator;
import de.topobyte.webgun.exceptions.PageNotFoundException;
import de.topobyte.webpaths.WebPath;

public class BaseGenerator extends Bootstrap4Generator
{

	public BaseGenerator(WebPath path)
	{
		super(path, false);
	}

	@Override
	public void generate() throws IOException, PageNotFoundException
	{
		super.generate();
		Element html = builder.getHtml();

		html.attr("lang", "en");

		Head head = builder.getHead();

		CacheBuster cacheBuster = Website.INSTANCE.getCacheBuster();

		FaviconUtil.addToHeader(head, "/" + cacheBuster.resolve("favicon.ico"));

		FaviconUtil.addToHeader(head,
				"/" + cacheBuster.resolve("images/favicon-16.png"), 16);
		FaviconUtil.addToHeader(head,
				"/" + cacheBuster.resolve("images/favicon-32.png"), 32);
		FaviconUtil.addToHeader(head,
				"/" + cacheBuster.resolve("images/favicon-48.png"), 48);
		FaviconUtil.addToHeader(head,
				"/" + cacheBuster.resolve("images/favicon-64.png"), 64);
		FaviconUtil.addToHeader(head,
				"/" + cacheBuster.resolve("images/favicon-96.png"), 96);

		head.ac(ElementBuilder
				.styleSheet("/" + cacheBuster.resolve("custom.css")));
	}

	protected void menu()
	{
		Menu menu = MainMenu.create(this);
		getBuilder().getBody().prependChild(menu);
	}

	protected void footer()
	{
		getBuilder().getBody().appendChild(new MainFooter(this));
	}

}
