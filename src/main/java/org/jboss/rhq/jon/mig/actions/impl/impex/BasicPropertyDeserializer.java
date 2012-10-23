package org.jboss.rhq.jon.mig.actions.impl.impex;

import java.lang.reflect.Type;

import org.jboss.rhq.jon.mig.model.impex.BasicProperty;
import org.jboss.rhq.jon.mig.model.impex.ListProperty;
import org.jboss.rhq.jon.mig.model.impex.MapProperty;
import org.jboss.rhq.jon.mig.model.impex.serialization.ListPropertyDeserializer;
import org.jboss.rhq.jon.mig.model.impex.serialization.MapPropertyDeserializer;

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
 * Date: 7/15/11
 * Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicPropertyDeserializer implements JsonDeserializer<BasicProperty> {
    static Type mapType = new TypeToken<MapProperty>() {
    }.getType();
    static Type listType = new TypeToken<ListProperty>() {
    }.getType();

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
}
