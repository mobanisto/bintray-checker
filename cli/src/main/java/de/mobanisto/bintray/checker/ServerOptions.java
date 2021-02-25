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
import org.apache.commons.cli.Options;

import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.parsing.ArgumentParseException;
import lombok.Getter;

public class ServerOptions
{

	private static final String OPTION_PORT = "port";

	public static void add(Options options)
	{
		OptionHelper.addL(options, OPTION_PORT, true, false,
				"the port to run the server (default: 8090)");
	}

	@Getter
	private int port = 8090;

	public static ServerOptions parse(CommandLine line)
			throws ArgumentParseException
	{
		String sPort = line.getOptionValue(OPTION_PORT);

		ServerOptions result = new ServerOptions();

		if (sPort != null) {
			try {
				result.port = Integer.parseInt(sPort);
			} catch (NumberFormatException e) {
				throw new ArgumentParseException(
						String.format("Unable to parse port: '%s'", sPort));
			}
		}

		return result;
	}

}
