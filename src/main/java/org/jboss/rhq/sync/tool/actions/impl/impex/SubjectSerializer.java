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

package org.jboss.rhq.sync.tool.actions.impl.impex;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.util.LogUtils;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;
import org.rhq.core.domain.resource.group.ResourceGroup;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class SubjectSerializer implements JsonSerializer<Subject>,JsonDeserializer<Subject> {

    private static Logger logger = Logger.getLogger(SubjectSerializer.class);

	@Override
	public Subject deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext ctx) throws JsonParseException {

		if ( logger.isDebugEnabled() )
			logger.debug("Deserialized invoked with:" + jsonElement.toString());
		Subject subject = new GsonBuilder().create().fromJson(jsonElement,type);
		LogUtils.logSubjectInstance(logger,subject, "Deserialized instance:");
		return subject;
	}

	@Override
	public JsonElement serialize(Subject subject, Type type,
			JsonSerializationContext ctx) {
        JsonObject result = new JsonObject();
        result.add("id",ctx.serialize(subject.getId(),Integer.class));
        result.add("name",ctx.serialize(subject.getName(),String.class));
        result.add("classname",ctx.serialize(subject.getClass().toString(),String.class));
        result.add("firstName",ctx.serialize(subject.getFirstName(),String.class));
        result.add("lastName",ctx.serialize(subject.getLastName(),String.class));
        result.add("emailAddress",ctx.serialize(subject.getEmailAddress(),String.class));
        result.add("department",ctx.serialize(subject.getDepartment(),String.class));
        result.add("phoneNumber",ctx.serialize(subject.getPhoneNumber(),String.class));
        result.add("smsAddress",ctx.serialize(subject.getSmsAddress(),String.class));
        result.add("fsystem",ctx.serialize(subject.getFsystem(),Boolean.class));
        result.add("factive",ctx.serialize(subject.getFactive(),Boolean.class));
        result.add("ownedGroups",ctx.serialize(groups(subject.getOwnedGroups()),Set.class));
        result.add("ldapRoles", ctx.serialize(subject.getLdapRoles(),Set.class)); //No support for LDAP Roles
        return result;
	}

	private Set<String> groups(Collection<ResourceGroup> list) {
        if ( list != null && ! list.isEmpty() ) {
            Set<String> groups = new HashSet<String>(list.size());
            for ( ResourceGroup group : list ) {
            	groups.add(group.getName());
            }
            return groups;
        }
        return new HashSet<String>(0);
	}

	/**
	 * <p>Commidity method to easily load a {@link Collection} of {@link Role}.</p>
	 *
	 * <p><em>This mostly takes care of the type marshalling necessary for JSON.</em></p>
	 *
	 * @param jsonAsString JSON content as a simple {@link String}.
	 * @return
	 */
    public static Collection<Subject> loadJsonRolesCollections(String jsonAsString) {
        Type collectionType = new TypeToken<Collection<Role>>(){}.getType();
    	return new GsonBuilder().create().fromJson(jsonAsString, collectionType);
    }

}
