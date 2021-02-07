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

package de.mobanisto.bintray.checker;

import de.mobanisto.bintray.checker.maven.Data;
import lombok.Getter;
import lombok.Setter;

public class Website
{

	public static final String TITLE = "Bintray Checker";

	public static final Website INSTANCE = new Website();

	@Getter
	@Setter
	private CacheBuster cacheBuster;

	@Getter
	@Setter
	private Data data;

}
