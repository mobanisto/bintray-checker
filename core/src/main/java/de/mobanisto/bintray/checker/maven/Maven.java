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

// Copied from topobyte:maven-util
public class Maven
{

	public static String URL_CENTRAL = "http://repo1.maven.org/maven2";

	public static String FILENAME_METADATA = "maven-metadata.xml";

	public static String SCOPE_COMPILE = "compile";
	public static String SCOPE_TEST = "test";
	public static String SCOPE_RUNTIME = "runtime";
	public static String SCOPE_PROVIDED = "provided";
	public static String SCOPE_SYSTEM = "system";

	public static String CLASS_JAR = "";
	public static String CLASS_SOURCES = "sources";

}
