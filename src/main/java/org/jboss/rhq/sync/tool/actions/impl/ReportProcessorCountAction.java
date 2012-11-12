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

package org.jboss.rhq.sync.tool.actions.impl;


import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.jboss.rhq.sync.tool.util.ProcessorCountTemplate;
import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 10.06.11
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
public class ReportProcessorCountAction extends AbstractJONAction {
    private ProcessorCountTemplate htmlTemplate;
    private Map<String, String> map;

    public ReportProcessorCountAction() {

        map = new HashMap<String, String>();
        map.put("" + 76052, "-");
        map.put("" + 76062, "-");
        map.put("" + 76054, "-");
        map.put("" + 76061, "-");
        map.put("" + 76053, "-");
        map.put("" + 76055, "-");
        map.put("" + 76051, "-");
        map.put("" + 65803, "-");
        map.put("" + 65806, "-");
        map.put("" + 58476, "-");
        map.put("" + 68691, "-");
        map.put("" + 68701, "-");
        map.put("" + 58500, "-");
        map.put("" + 58501, "-");
        map.put("" + 58485, "-");
        map.put("" + 58479, "-");
        map.put("" + 58486, "-");
        map.put("" + 68700, "-");
        map.put("" + 58475, "-");
        map.put("" + 58497, "-");
        map.put("" + 58481, "-");
        map.put("" + 58496, "-");
        map.put("" + 68698, "-");
        map.put("" + 68692, "-");
        map.put("" + 68697, "-");
        map.put("" + 76071, "-");
        map.put("" + 58482, "-");
        map.put("" + 68705, "-");
        map.put("" + 58473, "-");
        map.put("" + 58506, "-");
        map.put("" + 58505, "-");
        map.put("" + 58499, "-");
        map.put("" + 58503, "-");
        map.put("" + 29605, "-");
        map.put("" + 25875, "-");
        map.put("" + 25873, "-");
        map.put("" + 29661, "-");
        map.put("" + 29662, "-");
        map.put("" + 29663, "-");
        map.put("" + 76064, "-");
        map.put("" + 29607, "-");
        map.put("" + 29606, "-");
        map.put("" + 29664, "-");
        map.put("" + 29610, "-");
        map.put("" + 25880, "-");
        map.put("" + 25883, "-");
        map.put("" + 25884, "-");
        map.put("" + 25923, "-");
        map.put("" + 25921, "-");
        map.put("" + 25926, "-");
        map.put("" + 25925, "-");
        map.put("" + 25920, "-");
        map.put("" + 25929, "-");
        map.put("" + 25928, "-");
        map.put("" + 25927, "-");
        map.put("" + 25922, "-");
        map.put("" + 25924, "-");
        map.put("" + 74541, "-");
        map.put("" + 78413, "-");
        map.put("" + 74774, "-");
        map.put("" + 35241, "-");
        map.put("" + 67571, "-");
        map.put("" + 35251, "-");
        map.put("" + 74773, "-");
        map.put("" + 74771, "-");
        map.put("" + 74772, "-");
        map.put("" + 29603, "-");
        map.put("" + 74781, "-");
        map.put("" + 74782, "-");
        map.put("" + 74783, "-");
        map.put("" + 74784, "-");
        map.put("" + 37334, "-");
        map.put("" + 37333, "-");
        map.put("" + 36693, "-");
        map.put("" + 76063, "-");
        map.put("" + 67321, "-");
        map.put("" + 67322, "-");
        map.put("" + 66627, "-");
        map.put("" + 66628, "-");
        map.put("" + 34961, "-");
        map.put("" + 25893, "-");
        map.put("" + 67821, "-");
        map.put("" + 67822, "-");
        map.put("" + 74424, "-");
        map.put("" + 67991, "-");
        map.put("" + 25874, "-");
        map.put("" + 25877, "-");
        map.put("" + 25876, "-");
        map.put("" + 29604, "-");
        map.put("" + 25823, "-");
        map.put("" + 22975, "-");
        map.put("" + 58498, "-");
        map.put("" + 23000, "-");
        map.put("" + 58502, "-");
        map.put("" + 68704, "-");
        map.put("" + 68707, "-");
        map.put("" + 68694, "-");
        map.put("" + 68699, "-");
        map.put("" + 76296, "-");
        map.put("" + 75741, "-");
        map.put("" + 75801, "-");
        map.put("" + 77143, "-");
        map.put("" + 77253, "-");
        map.put("" + 77146, "-");
        map.put("" + 76293, "-");
        map.put("" + 68703, "-");
        map.put("" + 68696, "-");
        map.put("" + 68695, "-");
        map.put("" + 68706, "-");
        map.put("" + 36074, "-");
        map.put("" + 36073, "-");
        map.put("" + 36075, "-");
        map.put("" + 38451, "-");
        map.put("" + 36883, "-");
        map.put("" + 23005, "-");
        map.put("" + 22977, "-");
        map.put("" + 22982, "-");
        map.put("" + 53631, "-");
        map.put("" + 23013, "-");
        map.put("" + 23004, "-");
        map.put("" + 23016, "-");
        map.put("" + 23021, "-");
        map.put("" + 23024, "-");
        map.put("" + 23019, "-");
        map.put("" + 36403, "-");
        map.put("" + 56093, "-");
        map.put("" + 56091, "-");
        map.put("" + 56092, "-");
        map.put("" + 39921, "-");
        map.put("" + 58477, "-");
        map.put("" + 58478, "-");
        map.put("" + 58480, "-");
        map.put("" + 58483, "-");
        map.put("" + 58474, "-");
        map.put("" + 58493, "-");
        map.put("" + 58495, "-");
        map.put("" + 58494, "-");
        map.put("" + 58508, "-");
        map.put("" + 58507, "-");
        map.put("" + 58504, "-");
        map.put("" + 58484, "-");
        map.put("" + 58487, "-");
        map.put("" + 58488, "-");
        map.put("" + 22716, "-");
        map.put("" + 23003, "-");
        map.put("" + 23018, "-");
        map.put("" + 23017, "-");
        map.put("" + 22980, "-");
        map.put("" + 39821, "-");
        map.put("" + 39801, "-");
        map.put("" + 22999, "-");
        map.put("" + 22993, "-");
        map.put("" + 22997, "-");
        map.put("" + 22998, "-");
        map.put("" + 22995, "-");
        map.put("" + 22996, "-");
        map.put("" + 23022, "-");
        map.put("" + 22984, "-");
        map.put("" + 23002, "-");
        map.put("" + 23020, "-");
        map.put("" + 22981, "-");
        map.put("" + 22983, "-");
        map.put("" + 22973, "-");
        map.put("" + 22979, "-");
        map.put("" + 23025, "-");
        map.put("" + 23015, "-");
        map.put("" + 23014, "-");
        map.put("" + 22974, "-");
        map.put("" + 22976, "-");
        map.put("" + 22978, "-");
        map.put("" + 53621, "-");
        map.put("" + 63008, "-");
        map.put("" + 62844, "-");
        map.put("" + 23001, "-");
        map.put("" + 29723, "-");
        map.put("" + 29722, "-");
        map.put("" + 29700, "-");
        map.put("" + 29699, "-");
        map.put("" + 29721, "-");
        map.put("" + 72623, "-");
        map.put("" + 72683, "-");
        map.put("" + 72703, "-");
        map.put("" + 78741, "-");
        map.put("" + 65323, "-");
        map.put("" + 65432, "-");
        map.put("" + 74361, "-");
        map.put("" + 74213, "-");
        map.put("" + 73844, "-");
        map.put("" + 75251, "-");
        map.put("" + 75308, "-");
        map.put("" + 75309, "-");
        map.put("" + 75321, "-");
        map.put("" + 75611, "-");
        map.put("" + 75431, "-");
        map.put("" + 75487, "-");
        map.put("" + 64211, "-");
        map.put("" + 72422, "-");
        map.put("" + 72423, "-");
        map.put("" + 72424, "-");
        map.put("" + 72425, "-");
        map.put("" + 72426, "-");
        map.put("" + 72427, "-");
        map.put("" + 72428, "-");
        map.put("" + 72429, "-");
        map.put("" + 72430, "-");
        map.put("" + 75605, "-");
        map.put("" + 73852, "-");
        map.put("" + 73967, "-");
        map.put("" + 74024, "-");
        map.put("" + 78541, "-");
        map.put("" + 78531, "-");

    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        JbossAsResourceQuery query = new ResourceQueryImpl();
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd_HH_mm");

        this.htmlTemplate = new ProcessorCountTemplate("templates/ProccessorCount.tmpl", "processorCount" + df.format(new Date()) + ".html");
        List<Resource> list = query.getAllInventoriedJBossAS(null, null, null);
        Map<String, Resource> epp = new HashMap<String, Resource>();
        Map<String, Resource> soa = new HashMap<String, Resource>();
        Map<String, Resource> eap = new HashMap<String, Resource>();
        Map<String, Resource> other = new HashMap<String, Resource>();
        int eapCount = 0;
        int soaCount = 0;
        int eppCount = 0;
        int otherCount = 0;
        Map<String, Integer> temper = new HashMap<String, Integer>();
        for (Resource resource : list) {

            String key = resource.getResourceKey();

            if (key.startsWith("/opt/jboss/jbepp")) {
                epp.put(resource.getName(), resource);
                Integer count = temper.get("epp" + resource.getParentResource().getId());
                map.remove("" + resource.getId());
                if (count == null) {
                    int cpu = query.getCpuCount(resource.getParentResource().getId());
                    temper.put("epp" + resource.getParentResource().getId(), new Integer(cpu));
                    eppCount = eppCount + cpu;
                }
            } else if (key.startsWith("/opt/jboss/jbeap-")) {
                map.remove("" + resource.getId());
                eap.put(resource.getName(), resource);
                Integer count = temper.get("eap" + resource.getParentResource().getId());
                if (count == null) {
                    int cpu = query.getCpuCount(resource.getParentResource().getId());
                    temper.put("eap" + resource.getParentResource().getId(), new Integer(cpu));
                    eapCount = eapCount + cpu;
                }
            } else if (key.startsWith("/opt/jboss/jboss-soa-p")) {
                map.remove("" + resource.getId());
                soa.put(resource.getName(), resource);
                Integer count = temper.get("soa" + resource.getParentResource().getId());
                if (count == null) {
                    int cpu = query.getCpuCount(resource.getParentResource().getId());
                    temper.put("soa" + resource.getParentResource().getId(), new Integer(cpu));
                    soaCount = soaCount + cpu;
                }
            } else {
                map.remove("" + resource.getId());

                other.put(resource.getName(), resource);
                //otherCount=otherCount+cpu;
            }

            System.out.println();
        }
        System.out.println("report");

        System.out.println("eap =" + eap.size());
        System.out.println("epp =" + epp.size());
        System.out.println("soap =" + soa.size());
        System.out.println("other =" + other.size());

        addToTemplate("eap", eap.values(), eapCount);
        addToTemplate("epp", epp.values(), eppCount);
        addToTemplate("soa", soa.values(), soaCount);
        addToTemplate("other", other.values(), otherCount);
        htmlTemplate.printTemplate();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void addToTemplate(String group, Collection<Resource> resources, int cpuCount) {
        htmlTemplate.addHeader(group, "" + resources.size());

        for (Resource resource : resources) {
            htmlTemplate.add(group, resource.getName(), resource.getResourceKey(),
                    resource.getVersion(),
                    resource.getParentResource().getName());
        }
        htmlTemplate.addSummary(group, "" + cpuCount);

    }

    public static void main(String[] args) {
        JONAction t = new ReportProcessorCountAction();
        t.doAction(null);
    }
}
