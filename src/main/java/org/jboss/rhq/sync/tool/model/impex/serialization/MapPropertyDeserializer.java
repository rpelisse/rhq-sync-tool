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
import java.util.Map;

import org.jboss.rhq.sync.tool.actions.impl.impex.BasicPropertyDeserializer;
import org.jboss.rhq.sync.tool.model.impex.BasicProperty;
import org.jboss.rhq.sync.tool.model.impex.MapProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/21/11
 * Time: 12:40 AM
 * @see BasicPropertyTypeSerializeAdapter
 */
@Deprecated
public class MapPropertyDeserializer implements JsonDeserializer<MapProperty> {
    @Override
    public MapProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(BasicProperty.class, new BasicPropertyDeserializer());
        Gson gson = gb.create();
        Type listType = new TypeToken<Map<String, BasicProperty>>() {
        }.getType();
        //    gson.fromJson(jsonElement,ListProperty)
        Map<String, BasicProperty> map = gson.fromJson(((JsonObject) jsonElement).get("propertyMap"), listType);
        String name = ((JsonObject) jsonElement).get("key").getAsString();
        MapProperty mapProperty = new MapProperty();
        mapProperty.setPropertyMap(map);

        mapProperty.setKey(name);
        return mapProperty;
    }
}
