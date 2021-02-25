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

package de.mobanisto.bintray.checker;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class RunEmbeddedServer
{

	private static final String HELP_MESSAGE = RunEmbeddedServer.class
			.getSimpleName() + " [options] ";

	private static Options options = new Options();
	static {
		ServerOptions.add(options);
	}

	public static void main(String[] args) throws Exception
	{
		CommandLine line = null;
		try {
			line = new DefaultParser().parse(options, args);
		} catch (ParseException e) {
			printHelpAndExit("unable to parse command line: " + e.getMessage());
		}
		if (line == null) {
			return;
		}

		ServerOptions serverOptions = ServerOptions.parse(line);

		EmbeddedServer server = new EmbeddedServer();
		server.setPort(serverOptions.getPort());
		server.start(false);
	}

	private static void printHelpAndExit(String message)
	{
		System.out.println(message);
		new HelpFormatter().printHelp(HELP_MESSAGE, options);
		System.exit(1);
	}

}
