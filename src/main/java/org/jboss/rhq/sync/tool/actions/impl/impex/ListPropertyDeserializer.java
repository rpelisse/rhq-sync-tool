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

package org.jboss.rhq.sync.tool.actions.impl.impex;

import java.lang.reflect.Type;
import java.util.List;

import org.jboss.rhq.sync.tool.model.impex.BasicProperty;
import org.jboss.rhq.sync.tool.model.impex.ListProperty;

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
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListPropertyDeserializer implements JsonDeserializer<ListProperty> {
	
    @Override
    public ListProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(BasicProperty.class, new BasicPropertyDeserializer());
        Gson gson = gb.create();
        Type listType = new TypeToken<List<BasicProperty>>() {
        }.getType();
        //    gson.fromJson(jsonElement,ListProperty)
        List<BasicProperty> lister = gson.fromJson(((JsonObject) jsonElement).get("propertyList"), listType);
        String name = ((JsonObject) jsonElement).get("key").getAsString();
        ListProperty listProperty = new ListProperty();
        listProperty.setPropertyList(lister);

        listProperty.setKey(name);
        return listProperty;
    }
}
