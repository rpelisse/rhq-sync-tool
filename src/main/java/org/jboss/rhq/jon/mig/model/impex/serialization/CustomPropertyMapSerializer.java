package org.jboss.rhq.jon.mig.model.impex.serialization;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.model.impex.BasicProperty;
import org.jboss.rhq.jon.mig.model.impex.ListProperty;
import org.jboss.rhq.jon.mig.model.impex.MapProperty;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 12/6/11
 * Time: 10:23 PM
 * @see BasicPropertyTypeSerializeAdapter
 */
@Deprecated
public class CustomPropertyMapSerializer implements JsonSerializer<Map<String, BasicProperty>> {

    public JsonElement serialize(Map<String, BasicProperty> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray newAR = new JsonArray();
        Gson gg = new Gson();
        Collection<BasicProperty> props = src.values();
        for(BasicProperty objToSerialize: props){
            JsonElement jsonobj = new JsonObject();

            if (objToSerialize instanceof ListProperty) {
                logger.debug("Serializing  " + ListProperty.class.toString());
                if(((ListProperty) objToSerialize).equals("logEventSources"))
                    System.out.println();
                jsonobj = gg.toJsonTree(((ListProperty) objToSerialize), ListProperty.class);
            } else if (objToSerialize instanceof MapProperty) {
                logger.debug("Serializing  " + MapProperty.class.toString());
                jsonobj = gg.toJsonTree(((MapProperty) objToSerialize), MapProperty.class);
            } else if (objToSerialize instanceof BasicProperty) {
                logger.debug("Serializing  " + BasicProperty.class.toString());
                jsonobj = gg.toJsonTree(((BasicProperty) objToSerialize), BasicProperty.class);

            } else {
                jsonobj = gg.toJsonTree((objToSerialize));
            }


        }
        for (int i = 0; i < src.size(); i++) {
            Object objToSerialize = src.get(i);
            JsonElement jsonobj = new JsonObject();


            if (objToSerialize instanceof ListProperty) {
                logger.debug("Serializing  " + ListProperty.class.toString());
                  if(((ListProperty) objToSerialize).equals("logEventSources")){
                System.out.println();
            }
                jsonobj = gg.toJsonTree(((ListProperty) objToSerialize), ListProperty.class);
            } else if (objToSerialize instanceof MapProperty) {
                logger.debug("Serializing  " + MapProperty.class.toString());
                jsonobj = gg.toJsonTree(((MapProperty) objToSerialize), MapProperty.class);


            } else if (objToSerialize instanceof BasicProperty) {
                logger.debug("Serializing  " + BasicProperty.class.toString());
                jsonobj = gg.toJsonTree(((BasicProperty) objToSerialize), BasicProperty.class);

            } else {
                jsonobj = gg.toJsonTree((objToSerialize));
            }
            newAR.add(jsonobj);
        }
        return newAR;
    }

    private static Logger logger = Logger
            .getLogger(CustomPropertyMapSerializer.class);


}