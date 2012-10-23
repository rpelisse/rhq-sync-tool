package org.jboss.rhq.sync.tool.actions.impl.impex;

import java.lang.reflect.Type;
import java.util.Map;

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
 * To change this template use File | Settings | File Templates.
 */
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
