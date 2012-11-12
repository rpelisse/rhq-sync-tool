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
import java.util.List;
import java.util.Map;

import org.jboss.rhq.sync.tool.model.impex.BasicProperty;
import org.jboss.rhq.sync.tool.model.impex.ListProperty;
import org.jboss.rhq.sync.tool.model.impex.MapProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 12/6/11
 * Time: 11:00 PM
 *   Serializer adapter for type of BasicProperty
 */
public class BasicPropertyTypeSerializeAdapter implements JsonSerializer<BasicProperty>, JsonDeserializer<BasicProperty> {
      static Type mapType = new TypeToken<MapProperty>() {
    }.getType();
    static Type listType = new TypeToken<ListProperty>() {
    }.getType();
    
    @SuppressWarnings("deprecation")
	@Override
    public BasicProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
          GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(ListProperty.class, new ListPropertyDeserializer());
        gb.registerTypeAdapter(MapProperty.class, new MapPropertyDeserializer());
        Gson gson = gb.create();

        gson.fromJson(jsonElement, BasicProperty.class);
        BasicProperty g = gson.fromJson(jsonElement, BasicProperty.class);
        JsonElement map = ((JsonObject) jsonElement).get("propertyMap");
        JsonElement list = ((JsonObject) jsonElement).get("propertyList");
        if (g.getValue() == null && map != null) {
            ;
            return gson.fromJson(jsonElement, mapType);
        } else if (g.getValue() == null && list != null) {
            return gson.fromJson(jsonElement, listType);
        } else
            return g;
    }

    @Override
    public JsonElement serialize(BasicProperty property, Type type, JsonSerializationContext jsonSerializationContext) {
           JsonObject result = new JsonObject();
           result.add("key", jsonSerializationContext.serialize(property.getKey(), String.class));
           if (property instanceof MapProperty) {
             result.add("propertyMap", jsonSerializationContext.serialize(((MapProperty) property).getPropertyMap(), Map.class));
           }else if (property instanceof ListProperty) {
               result.add("propertyList", jsonSerializationContext.serialize(((ListProperty) property).getPropertyList(), List.class));
             }else{
               result.add("value", jsonSerializationContext.serialize(property.getValue(), String.class));
           }
           return result;
    }
}
