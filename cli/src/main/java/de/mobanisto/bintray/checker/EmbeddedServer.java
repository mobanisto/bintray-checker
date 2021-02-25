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

import java.nio.file.Path;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.SessionTrackingMode;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.ResourceCollection;

import de.mobanisto.bintray.checker.resources.ClassPathResources;
import de.topobyte.jetty.utils.JettyUtils;
import de.topobyte.jetty.utils.VirtualPathResource;
import de.topobyte.system.utils.SystemPaths;

public class EmbeddedServer
{

	private Server server;

	public void start(boolean includeDirectories) throws Exception
	{
		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8090);

		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.setContextPath("/");

		servletContextHandler.addEventListener(new InitListener());
		servletContextHandler.addFilter(RootFilter.class.getName(), "/*",
				EnumSet.noneOf(DispatcherType.class));

		servletContextHandler.addServlet(IndexServlet.class.getName(), "/*");

		JettyUtils.addDefaultServlet(servletContextHandler);

		JettyUtils.setSessionTracking(servletContextHandler,
				SessionTrackingMode.COOKIE);

		// this will only be used for testing, the CLI uses the classpath
		Path pathProject = SystemPaths.CWD.resolve("../").normalize();
		Path pathCore = pathProject.resolve("core");
		Path pathResYarn = pathCore.resolve("src/assets/vendor");

		// includeDirectories is true when running from the test
		if (includeDirectories) {
			ResourceCollection allResources = new ResourceCollection(
					new PathResource(pathCore.resolve("build/static")),
					new VirtualPathResource(pathResYarn, "/client"));

			servletContextHandler.setBaseResource(allResources);
		} else {
			ResourceCollection allResources = new ResourceCollection(
					new ClassPathResources());
			servletContextHandler.setBaseResource(allResources);
		}

		server.setHandler(servletContextHandler);

		server.setConnectors(new Connector[] { connector });
		server.start();
		server.join();
	}

}
