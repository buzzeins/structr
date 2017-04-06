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
package org.structr.api.config;

import org.structr.api.util.html.Attr;
import org.structr.api.util.html.Tag;

/**
 * A configuration setting with a key and a type.
 */
public class IntegerSetting extends Setting<Integer> {

	/**
	 * Constructor to create an empty IntegerSetting with NO default value.
	 *
	 * @param group
	 * @param key
	 */
	public IntegerSetting(final SettingsGroup group, final String key) {
		this(group, key, null);
	}

	/**
	 * Constructor to create an IntegerSetting WITH default value.
	 *
	 * @param group
	 * @param key
	 * @param value
	 */
	public IntegerSetting(final SettingsGroup group, final String key, final Integer value) {
		this(group, null, key, value);
	}

	/**
	 * Constructor to create an IntegerSetting with category name and default value.
	 * @param group
	 * @param categoryName
	 * @param key
	 * @param value
	 */
	public IntegerSetting(final SettingsGroup group, final String categoryName, final String key, final Integer value) {
		super(group, categoryName, key, value);
	}

	@Override
	public void render(final Tag parent) {

		final Tag group = parent.block("div").css("form-group");

		group.block("label").text(getKey());
		group.empty("input").attr(new Attr("type", "text"), new Attr("value", getValue()));

		renderResetButton(group);
	}

	@Override
	public void fromString(final String source) {
		setValue(Integer.parseInt(source));
	}
}
