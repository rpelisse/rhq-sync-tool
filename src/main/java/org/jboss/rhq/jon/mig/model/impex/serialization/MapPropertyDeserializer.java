package org.jboss.rhq.jon.mig.model.impex.serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import org.jboss.rhq.jon.mig.actions.impl.impex.BasicPropertyDeserializer;
import org.jboss.rhq.jon.mig.model.impex.BasicProperty;
import org.jboss.rhq.jon.mig.model.impex.MapProperty;

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
