package org.jboss.rhq.jon.mig.model.impex.serialization;

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
