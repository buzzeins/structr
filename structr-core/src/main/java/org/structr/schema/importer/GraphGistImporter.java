/**
 * Copyright (C) 2010-2016 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.schema.importer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.structr.api.DatabaseService;
import org.structr.api.Transaction;
import org.structr.common.error.FrameworkException;
import org.structr.core.app.App;
import org.structr.core.app.StructrApp;
import org.structr.core.graph.MaintenanceCommand;

/**
 *
 *
 */
public class GraphGistImporter extends SchemaImporter implements MaintenanceCommand {

	private static final Logger logger = Logger.getLogger(GraphGistImporter.class.getName());

	@Override
	public void execute(Map<String, Object> attributes) throws FrameworkException {

		final String fileName = (String)attributes.get("file");
		final String source   = (String)attributes.get("source");
		final String url      = (String)attributes.get("url");

		if (fileName == null && source == null && url == null) {
			throw new FrameworkException(422, "Please supply file, url or source parameter.");
		}

		if (fileName != null && source != null) {
			throw new FrameworkException(422, "Please supply only one of file, url or source.");
		}

		if (fileName != null && url != null) {
			throw new FrameworkException(422, "Please supply only one of file, url or source.");
		}

		if (url != null && source != null) {
			throw new FrameworkException(422, "Please supply only one of file, url or source.");
		}

		try {

			if (fileName != null) {

				GraphGistImporter.importCypher(extractSources(new FileInputStream(fileName)));

			} else if (url != null) {

				GraphGistImporter.importCypher(extractSources(new URL(url).openStream()));

			} else if (source != null) {

				GraphGistImporter.importCypher(extractSources(new ByteArrayInputStream(source.getBytes())));
			}

		} catch (IOException ioex) {
			//iologger.log(Level.WARNING, "", ex);
			logger.log(Level.FINE, "Filename: " + fileName + ", URL: " + url + ", source: " + source, ioex);
		}

		analyzeSchema();
	}


	public static void importCypher(final List<String> sources) {

		final App app                 = StructrApp.getInstance();
		final DatabaseService graphDb = app.getDatabaseService();

		// nothing to do
		if (sources.isEmpty()) {
			return;
		}

		// first step: execute cypher queries
		for (final String source : sources) {

			try (final Transaction tx = graphDb.beginTx()) {

				// be very tolerant here, just execute everything
				graphDb.execute(source);
				tx.success();

			} catch (Throwable t) {
				// ignore
				logger.log(Level.WARNING, "", t);
			}
		}
	}

	@Override
	public boolean requiresEnclosingTransaction() {
		return false;
	}
}
