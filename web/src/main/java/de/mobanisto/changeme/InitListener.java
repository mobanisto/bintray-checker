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

package de.mobanisto.changeme;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import de.mobanisto.changeme.maven.Data;
import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.melon.commons.io.Resources;

@WebListener
public class InitListener implements ServletContextListener
{

	final static Logger logger = LoggerFactory.getLogger(InitListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		logger.info("context initialized");
		long start = System.currentTimeMillis();

		logger.info("setting up website factories");
		Website.INSTANCE.setCacheBuster(filename -> {
			return CacheBusting.resolve(filename);
		});

		logger.info("loading configuration...");
		Properties config = new Properties();
		try (InputStream input = Resources.stream("config.properties")) {
			config.load(input);
		} catch (Throwable e) {
			logger.error("Unable to load configuration", e);
		}

		logger.info("loading secure configuration...");
		Properties secureConfig = new Properties();
		try (InputStream input = Resources.stream("secure.properties")) {
			secureConfig.load(input);
		} catch (Throwable e) {
			logger.error("Unable to load secure configuration", e);
		}

		logger.info("initializing data...");
		Website.INSTANCE.setData(new Data());

		long stop = System.currentTimeMillis();

		logger.info("done");
		logger.info(String.format("Initialized in %.2f seconds",
				(stop - start) / 1000d));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		logger.info("context destroyed");

		logger.info("shutting down Logback");
		LoggerContext loggerContext = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		loggerContext.stop();
	}

}
