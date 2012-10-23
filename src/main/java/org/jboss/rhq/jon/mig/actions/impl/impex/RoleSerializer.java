package org.jboss.rhq.jon.mig.actions.impl.impex;

import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.util.LogUtils;
import org.rhq.core.domain.authz.Role;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class RoleSerializer implements JsonSerializer<Role> {

    private static Logger logger = Logger.getLogger(RoleSerializer.class);


    @Override
    public JsonElement serialize(Role role, Type type,JsonSerializationContext ctx) {
        if ( logger.isDebugEnabled() )
            LogUtils.logRoleInstance(logger,role, "Serializing...");
        JsonObject result = new JsonObject();
        result.add("id",ctx.serialize(role.getId(),Integer.class));
        result.add("name",ctx.serialize(role.getName(),String.class));
        result.add("classname",ctx.serialize(role.getClass().toString(),String.class));
        result.add("description",ctx.serialize(role.getDescription(),String.class));
        result.add("fsystem",ctx.serialize(role.getFsystem(),Boolean.class));
        result.add("memberCount",ctx.serialize(role.getMemberCount(),Integer.class));
        result.add("permission",ctx.serialize(role.getPermissions(),Collection.class));
        result.add("subjects",ctx.serialize(role.getSubjects(),Collection.class));
        return result;
    }

    /**
     * <p>Commidity method to easily load a {@link Collection} of {@link Role}.</p>
     *
     * <p><em>This mostly takes care of the type marshalling necessary for JSON.</em></p>
     *
     * @param jsonAsString JSON content as a simple {@link String}.
     * @return
     */
    public static Collection<Role> loadJsonRolesCollections(String jsonAsString) {
        Type collectionType = new TypeToken<Collection<Role>>(){}.getType();
        return new JsonIO().getGson().create().fromJson(jsonAsString, collectionType);
    }

}
