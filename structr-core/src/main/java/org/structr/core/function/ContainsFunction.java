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
package org.structr.core.function;

import java.util.Collection;
import org.apache.commons.lang3.ArrayUtils;
import org.structr.common.error.FrameworkException;
import org.structr.schema.action.ActionContext;
import org.structr.schema.action.Function;

/**
 *
 */
public class ContainsFunction extends Function<Object, Object> {

	public static final String ERROR_MESSAGE_CONTAINS = "Usage: ${contains(string, word)} or ${contains(collection, element)}. Example: ${contains(this.name, \"the\")} or ${contains(find('Page'), page)}";

	@Override
	public String getName() {
		return "contains()";
	}

	@Override
	public Object apply(final ActionContext ctx, final Object caller, final Object[] sources) throws FrameworkException {

		try {
			if (!arrayHasLengthAndAllElementsNotNull(sources, 2)) {

				return false;
			}

			if (sources[0] instanceof String && sources[1] instanceof String) {

				final String source = sources[0].toString();
				final String part = sources[1].toString();

				return source.contains(part);

			} else if (sources[0] instanceof Collection) {

				final Collection collection = (Collection)sources[0];
				return collection.contains(sources[1]);

			} else if (sources[0].getClass().isArray()) {

				return ArrayUtils.contains((Object[])sources[0], sources[1]);
			}

		} catch (final IllegalArgumentException e) {

			logParameterError(caller, sources, ctx.isJavaScriptContext());
			
			return usage(ctx.isJavaScriptContext());

		}
		
		return false;

	}


	@Override
	public String usage(boolean inJavaScriptContext) {
		return ERROR_MESSAGE_CONTAINS;
	}

	@Override
	public String shortDescription() {
		return "Returns true if the given string or collection contains an element";
	}

}
