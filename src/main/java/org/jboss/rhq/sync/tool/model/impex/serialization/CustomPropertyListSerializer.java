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

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.impl.impex.BasicPropertyDeserializer;
import org.jboss.rhq.sync.tool.model.impex.BasicProperty;
import org.jboss.rhq.sync.tool.model.impex.ListProperty;
import org.jboss.rhq.sync.tool.model.impex.MapProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 12/4/11
 * Time: 10:52 PM
  * @see BasicPropertyTypeSerializeAdapter
 */
@Deprecated
public class CustomPropertyListSerializer implements JsonSerializer<List<BasicProperty>> {

    private Gson gson;

    public CustomPropertyListSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BasicProperty.class, new BasicPropertyDeserializer());
        //gsonBuilder.registerTypeAdapter(List.class, new CustomPropertyListSerializer());
        gson = gsonBuilder.create();
    }

    public JsonElement serialize(List<BasicProperty> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray newAR = new JsonArray();
        for (int i = 0; i < src.size(); i++) {
            Object objToSerialize = src.get(i);
            JsonElement jsonobj = new JsonObject();


            if (objToSerialize instanceof ListProperty) {
                logger.debug("Serializing  " + ListProperty.class.toString());
                  if(((ListProperty) objToSerialize).getKey().equals("logEventSources")){
                System.out.println();
            }
                jsonobj = gson.toJsonTree(( objToSerialize));
            } else if (objToSerialize instanceof MapProperty) {
                logger.debug("Serializing  " + MapProperty.class.toString());
                jsonobj = gson.toJsonTree(( objToSerialize), BasicProperty.class);


            } else if (objToSerialize instanceof BasicProperty) {
                logger.debug("Serializing  " + BasicProperty.class.toString());
                jsonobj = gson.toJsonTree(((BasicProperty) objToSerialize), BasicProperty.class);

            } else {
                jsonobj = gson.toJsonTree((objToSerialize));
            }
            newAR.add(jsonobj);
        }
        return newAR;
    }

    private static Logger logger = Logger
            .getLogger(CustomPropertyListSerializer.class);


}
