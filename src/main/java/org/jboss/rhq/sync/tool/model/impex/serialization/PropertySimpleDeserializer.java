/*
*
* RHQ Sync Tool
* Copyright (C) 2012-2013 Red Hat, Inc.
* All rights reserved.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License,
* version 2.1, as published by the Free Software Foundation.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License and the GNU Lesser General Public License
* for more details.
*
* You should have received a copy of the GNU General Public License
* and the GNU Lesser General Public License along with this program;
* if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*
*/

package org.jboss.rhq.sync.tool.model.impex.serialization;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.rhq.core.domain.configuration.PropertySimple;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PropertySimpleDeserializer implements JsonSerializer<PropertySimple>,JsonDeserializer<PropertySimple> {

    private static Logger logger = Logger.getLogger(PropertySimple.class);

	@Override
	public JsonElement serialize(PropertySimple property, Type type,
			JsonSerializationContext jsonSerializationContext) {
		logger.debug("Serializing instance of " + PropertySimple.class + ":[" + property.toString() + "]");
        JsonObject result = new JsonObject();
        result.add(property.getName(), jsonSerializationContext.serialize(property.getStringValue(), String.class));
		return result;
	}

	@Override
	public PropertySimple deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		logger.debug("Deserializing instance of " + PropertySimple.class + ":[" + jsonElement.toString() + "]");
        return new GsonBuilder().create().fromJson(jsonElement, PropertySimple.class);
	}
}
