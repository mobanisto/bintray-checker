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

package de.mobanisto.bintray.checker.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Document;

import de.mobanisto.bintray.checker.pages.other.ErrorGenerator;
import de.topobyte.jsoup.nodes.Element;
import de.topobyte.webpaths.WebPath;

public class ServletUtil
{

	public static void respond404(WebPath output, HttpServletResponse response,
			Void data) throws IOException
	{
		respond(404, output, response, content -> {
			ErrorUtil.write404(content);
		}, data);
	}

	public static void respond404(WebPath output, HttpServletResponse response,
			Consumer<Element<?>> contentGenerator, Void data) throws IOException
	{
		respond(404, output, response, contentGenerator, data);
	}

	public static void respond(int code, WebPath output,
			HttpServletResponse response, Consumer<Element<?>> contentGenerator,
			Void data) throws IOException
	{
		response.setStatus(code);

		response.setCharacterEncoding("UTF-8");

		PrintWriter writer = response.getWriter();

		ErrorGenerator generator = new ErrorGenerator(output);
		generator.generate();
		Element<?> content = generator.getContent();

		contentGenerator.accept(content);

		Document document = generator.getBuilder().getDocument();
		writer.write(document.toString());

		writer.close();
	}

}
