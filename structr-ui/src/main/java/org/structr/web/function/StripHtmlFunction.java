/**
 * Copyright (C) 2010-2017 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.web.function;

import org.structr.common.error.FrameworkException;
import org.structr.schema.action.ActionContext;
import org.structr.schema.action.Function;

/**
 *
 */
public class StripHtmlFunction extends Function<Object, Object> {

	public static final String ERROR_MESSAGE_STRIP_HTML    = "Usage: ${strip_html(html)}. Example: ${strip_html(\"<p>foo</p>\")}";
	public static final String ERROR_MESSAGE_STRIP_HTML_JS = "Usage: ${{Structr.strip_html(html)}}. Example: ${{Structr.strip_html(\"<p>foo</p>\")}}";

	@Override
	public String getName() {
		return "strip_html()";
	}

	@Override
	public Object apply(final ActionContext ctx, final Object caller, final Object[] sources) throws FrameworkException {

		try {
			
			if (!arrayHasLengthAndAllElementsNotNull(sources, 1)) {
				
				return null;
			}

			return sources[0].toString().replaceAll("\\<.*?>", "");

		} catch (final IllegalArgumentException e) {

			logParameterError(caller, sources, ctx.isJavaScriptContext());

			return usage(ctx.isJavaScriptContext());

		}

	}

	@Override
	public String usage(boolean inJavaScriptContext) {
		return (inJavaScriptContext ? ERROR_MESSAGE_STRIP_HTML_JS : ERROR_MESSAGE_STRIP_HTML);
	}

	@Override
	public String shortDescription() {
		return "Strips all (HTML) tags from the given string";
	}


}
