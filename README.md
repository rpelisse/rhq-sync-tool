rhq-sync-tool
=============

A tool for RHQ to export and import data from one server to another, using JSON as a transfer format.

How to build ?
==============

$ mvn package

How to test the tools ?
=======================

Inside the code, you can use Maven to run the tool:

$ mvn compile
$ mvn exec:java -Dexec.mainClass="org.jboss.rhq.sync.tool.RhqSyncTool" -Dexec.args="-d target/output/ -s http://localhost:7080/ -e -p rhqadmin -u rhqadmin  EXPORT -q ROLES"

TODO
====

* Urgent (for release):
** Configure assembly to generate proper zip file
** Add changelog plugin
** Release version 1.0
** Add spec to package as an RPM
* After release:
** Add proper static documentation (Awestruct ? Middleman ? MarkedDown ?)
** Make jon-mig.log location configurable
** Inspect dependencies, see if some more could be removed
** See if tools works for > 3.0.1
