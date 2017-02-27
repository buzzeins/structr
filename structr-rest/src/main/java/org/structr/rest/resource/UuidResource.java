/**
 * Copyright (C) 2010-2017 Structr GmbH
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
package org.structr.rest.resource;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.structr.common.SecurityContext;
import org.structr.common.error.FrameworkException;
import org.structr.core.GraphObject;
import org.structr.core.Result;
import org.structr.core.app.App;
import org.structr.core.app.StructrApp;
import org.structr.core.property.PropertyKey;
import org.structr.rest.exception.IllegalPathException;
import org.structr.rest.exception.NotFoundException;

//~--- classes ----------------------------------------------------------------

/**
 * Represents an exact UUID match.
 *
 *
 */
public class UuidResource extends FilterableResource {

	private static final Logger logger = LoggerFactory.getLogger(UuidResource.class.getName());

	//~--- fields ---------------------------------------------------------

	private String uuid = null;

	//~--- methods --------------------------------------------------------

	@Override
	public Result doGet(PropertyKey sortKey, boolean sortDescending, int pageSize, int page, String offsetId) throws FrameworkException {

		GraphObject obj = getEntity();
		if (obj != null) {

			return new Result(obj, isPrimitiveArray());

		}

		throw new NotFoundException("Entity with ID " + getUuid() + " not found");
	}

	@Override
	public boolean checkAndConfigure(String part, SecurityContext securityContext, HttpServletRequest request) {

		this.securityContext = securityContext;

		this.setUuid(part);

		return true;

	}

	@Override
	public Resource tryCombineWith(Resource next) throws FrameworkException {

		// do not allow nesting of "bare" uuid resource with type resource
		// as this will not do what the user expects to do.
		if (next instanceof TypeResource) {

			throw new IllegalPathException("Cannot resolve URL path, no type resource expected here");
		}

		return super.tryCombineWith(next);
	}

	public GraphObject getEntity() throws FrameworkException {

		final App app = StructrApp.getInstance(securityContext);

		GraphObject entity = app.nodeQuery().uuid(uuid).getFirst();
		if (entity == null) {

			entity = app.relationshipQuery().uuid(uuid).getFirst();
		}

		if (entity == null) {
			throw new NotFoundException("Entity with ID " + uuid + " not found");
		}

		return entity;
	}

	public String getUuid() {

		return uuid;

	}

	@Override
	public String getUriPart() {

		return uuid;

	}

	@Override
	public String getResourceSignature() {

		return "/";

	}

	@Override
	public boolean isCollectionResource() {

		return false;

	}

	//~--- set methods ----------------------------------------------------

	public void setUuid(String uuid) {

		this.uuid = uuid;

	}

}
