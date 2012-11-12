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

package org.jboss.rhq.sync.tool.actions.impl.impex.templates;

import static org.jboss.rhq.sync.tool.actions.JonActionResult.JonActionResultType.FAIL;
import static org.jboss.rhq.sync.tool.actions.JonActionResult.JonActionResultType.SUCCESS;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.JonGroupDefActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONExportAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.AbstractResourceInventoryExporter;
import org.jboss.rhq.sync.tool.query.GroupsQueryImpl;
import org.jboss.rhq.sync.tool.util.FileUtils;
import org.rhq.core.domain.resource.group.GroupDefinition;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 11/1/11
 * Time: 7:54 PM
 */
public class GroupDefinitionExportAction extends AbstractJONExportAction<Set<GroupDefinition>> {
	
    private static final String OUTPUT_FILENAME_PROPERTY_NAME = "user.export.filename";

    private static final Logger logger = Logger.getLogger(AbstractResourceInventoryExporter.class);

    public GroupDefinitionExportAction() {}
    
    public GroupDefinitionExportAction(LoginConfiguration loginConfiguration,
			BaseRemote baseRemote) {
    	super(loginConfiguration,baseRemote);
    }
    
	@Override
    protected JonActionResult.JonActionResultType perform(Map<String,String> values) throws RuntimeException {
       throw new UnsupportedOperationException();
    }
	
	private Set<GroupDefinition> queryGroups() {
		return new GroupsQueryImpl().exportAllGroupDefinitions();
	}
	
	private static void importGroups(Set<GroupDefinition> groupDefinitions) {
		new GroupsQueryImpl().importAllGroups(groupDefinitions);
	}
	
    @Override
    public final JonActionResult doAction(Map<String,String> values) {
        logger.debug("excuting peform method");
        JonGroupDefActionResult result = new JonGroupDefActionResult();
        try {
        	result.setGroupDefinitions( saveOnFile( queryGroups(), values)); 
            result.setResultType(SUCCESS);
        } catch (RuntimeException e) {
            logger.error(" perform method did not successfully complete. ");
            e.printStackTrace();
            result.setResultType(FAIL);
            addResultMessage("ERROR. perform method did not successfully complete. " + e.getMessage());
            return result;
        }
        logger.debug("Got Result " + result);
        logger.debug(" Logging out subject");
        baseRemote.logout();
        return result;
    }
	
	public static void main(String[] args) {
		logger.info("Running main from " + GroupDefinitionExportAction.class);
        GroupDefinitionExportAction a = new GroupDefinitionExportAction();
        final String outputFilename = "target/" + GroupDefinitionExportAction.class.getSimpleName() + ".output.json"; 
        
        Map<String,String> values = new HashMap<String, String>(1);
        values.put(OUTPUT_FILENAME_PROPERTY_NAME, outputFilename);
        JonGroupDefActionResult result = (JonGroupDefActionResult) a.doAction(values);
        for ( GroupDefinition def : result.getGroupDefinitions() ) {
        	logger.info("Group Info retrieved:" + def.getName());
        	def.setName(def.getName() + "exported");
        	def.setId(0);
        	logger.info("Changed to :" + def.getName() );
        }

//		TODO: Import
        logger.info("Group Defs exported to " + outputFilename);
        GroupDefinitionExportAction.importGroups(result.getGroupDefinitions());
/*        
        @SuppressWarnings("unchecked")
		Set<GroupDefinition> defs = new JsonIO().loadJson(values.get(OUTPUT_FILENAME_PROPERTY_NAME), new HashSet<GroupDefinition>().getClass());
        logger.info(defs.size() + " group definition loaded.");        
        for ( GroupDefinition def : result.getGroupDefinitions() ) {
        	logger.info("loaded groupDef:" + def.toString());
        }
  */      
        FileUtils.delete(outputFilename);

    }
}
