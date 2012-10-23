package org.jboss.rhq.jon.mig.actions.impl.impex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.rhq.jon.mig.actions.JONAction;
import org.rhq.core.domain.resource.Resource;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/22/11
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class JbossResourceImport extends AbstractResourceImporter {

    public JbossResourceImport() {
        super();
        registerPropertyOverride(new PropertyValueOverrideLogPath("logFilePath"));
    }

    @Override
    public List<Resource> filterResourceToUpdate(List<Resource> resourceToUpdate) {
        return resourceToUpdate;
    }

    /**todo
     * we need to define a super class called get resources.
     * we can then override this and filter based on the resource;s parent platform.
     * and return a cleaned list of resources that will be updated
     *
     *
     * also. we need to add to the list of property pre processors. those things than change the value before its commited
     *
     * @param args
     */

    public static void main(String[] args) {
        JONAction rs = new JbossResourceImport();
        Map<String,String> map = new HashMap<String, String>();
        map.put("fileName", "json/jbossRHQ_EXPORT.json");
        rs.doAction(map);
    }

}
