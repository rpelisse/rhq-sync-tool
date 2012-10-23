package org.jboss.rhq.sync.tool.model.impex.serialization;

import java.lang.reflect.Type;
import java.util.List;

import org.jboss.rhq.sync.tool.actions.impl.impex.BasicPropertyDeserializer;
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
