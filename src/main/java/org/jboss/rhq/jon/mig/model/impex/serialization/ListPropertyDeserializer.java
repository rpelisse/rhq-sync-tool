package org.jboss.rhq.jon.mig.model.impex.serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import org.jboss.rhq.jon.mig.actions.impl.impex.BasicPropertyDeserializer;
import org.jboss.rhq.jon.mig.model.impex.BasicProperty;
import org.jboss.rhq.jon.mig.model.impex.ListProperty;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/21/11
 * Time: 12:11 AM
 * @see BasicPropertyTypeSerializeAdapter
 */
@Deprecated
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
