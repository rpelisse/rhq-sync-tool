package org.jboss.rhq.jon.mig.actions.impl;


import org.jboss.rhq.jon.mig.actions.JonActionResult;
import org.jboss.rhq.jon.mig.query.ResourceQueryImpl;
import org.rhq.core.domain.resource.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 08.07.11
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class ListAllPlatformAgentsAction extends AbstractJONAction {
    private Map<String, Integer> mapWithDuplicates;
    private Map<String, Integer> mapWithout;

    public ListAllPlatformAgentsAction() {
        this.mapWithDuplicates = new HashMap<String, Integer>();
        this.mapWithout = new HashMap<String, Integer>();


    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        ResourceQueryImpl query = new ResourceQueryImpl();

        List<Resource> agents = query.getAllInventoriedRHQAgents("");
        for (int i = 0; i < agents.size(); i++) {
            Resource resource = agents.get(i);
            System.out.println(i + ") " + resource.getParentResource().getName() + ":" + resource.getName());
            addEntry(resource.getParentResource().getName());
        }
        System.out.println("------------------------------------------------------");
        System.out.println("------------------------------------------------------");
        System.out.println("\n\n\n");
        int count = 0;
        for (Map.Entry<String, Integer> stringIntegerEntry : mapWithDuplicates.entrySet()) {
            if (stringIntegerEntry.getValue() == 2) {
                System.out.println(":" + stringIntegerEntry);
                count++;
            } else {


            }

        }
        System.out.println("----------------------");

        for (Map.Entry<String, Integer> stringIntegerEntry : mapWithout.entrySet()) {
            System.out.println("--- " + stringIntegerEntry);
        }
        System.out.println("total=" + mapWithDuplicates.size() + ", with Duplicates=" + count);
        System.out.println("total without=" + mapWithout.size());


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void addEntry(String name) {
        if (mapWithDuplicates.get(name) != null) {
            Integer v = mapWithDuplicates.get(name);
            v++;
            mapWithDuplicates.put(name, v);
            mapWithout.remove(name);
        } else {

            mapWithout.put(name, 1);
            mapWithDuplicates.put(name, 1);
        }

    }

    public static void main(String[] args) {
        ListAllPlatformAgentsAction t = new ListAllPlatformAgentsAction();
        t.doAction(null);
    }
}
