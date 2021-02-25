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

package de.mobanisto.bintray.checker.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;

public class ClassPathResource extends DefaultResource
{

	private String path;

	public ClassPathResource(String path)
	{
		this.path = path;
	}

	private InputStream input()
	{
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(path);
	}

	private URL url()
	{
		return Thread.currentThread().getContextClassLoader().getResource(path);
	}

	@Override
	public void close()
	{

	}

	@Override
	public boolean exists()
	{
		try (InputStream is = input()) {
			return is != null;
		} catch (IOException e) {
			// ignore
		}
		return false;
	}

	@Override
	public boolean isDirectory()
	{
		return false;
	}

	@Override
	public long lastModified()
	{
		try {
			URLConnection connection = url().openConnection();
			long lastModified = connection.getLastModified();
			return lastModified;
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public long length()
	{
		return -1;
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return input();
	}

	@Override
	public ReadableByteChannel getReadableByteChannel() throws IOException
	{
		// can we implement this?
		return null;
	}

}
