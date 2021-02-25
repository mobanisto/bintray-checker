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

package de.mobanisto.bintray.checker.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

import org.eclipse.jetty.util.resource.Resource;

public class DefaultResource extends Resource
{

	@Override
	public boolean isContainedIn(Resource r) throws MalformedURLException
	{
		return false;
	}

	@Override
	public void close()
	{

	}

	@Override
	public boolean exists()
	{
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
		return 0;
	}

	@Override
	public long length()
	{
		return 0;
	}

	@Override
	public URL getURL()
	{
		return null;
	}

	@Override
	public File getFile() throws IOException
	{
		return null;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return null;
	}

	@Override
	public ReadableByteChannel getReadableByteChannel() throws IOException
	{
		return null;
	}

	@Override
	public boolean delete() throws SecurityException
	{
		return false;
	}

	@Override
	public boolean renameTo(Resource dest) throws SecurityException
	{
		return false;
	}

	@Override
	public String[] list()
	{
		return null;
	}

	@Override
	public Resource addPath(String path)
			throws IOException, MalformedURLException
	{
		return null;
	}

}
