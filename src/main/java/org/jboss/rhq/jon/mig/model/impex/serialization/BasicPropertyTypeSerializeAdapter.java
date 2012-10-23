package org.jboss.rhq.jon.mig.model.impex.serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jboss.rhq.jon.mig.model.impex.BasicProperty;
import org.jboss.rhq.jon.mig.model.impex.ListProperty;
import org.jboss.rhq.jon.mig.model.impex.MapProperty;

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
