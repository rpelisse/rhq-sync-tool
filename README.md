rhq-sync-tool
=============

A tool for RHQ to export and import data from one server to another, using JSON as a transfer format. This tool is released under the LGPL license.

How to build ?
==============

$ mvn package

How to test the tools ?
=======================

Inside the code, you can use Maven to run the tool:

$ mvn compile
$ mvn exec:java -Dexec.mainClass="org.jboss.rhq.sync.tool.RhqSyncTool" \
    -Dexec.args="-d target/output/ -s http://localhost:7080/ -e -p rhqadmin -u rhqadmin  EXPORT -q ROLES"
$ mvn exec:java -Dexec.mainClass="org.jboss.rhq.sync.tool.RhqSyncTool"  \
    -Dexec.args="-d target/output/ -s http://localhost:7080/ -e -p rhqadmin -u rhqadmin  IMPORT -q ROLES -f target/output/roles.json"

How to I make a release and test it ?
=====================================

$ mvn clean assembly::assembly  # do not forget the 'clean' !
$ unzip target/rhq-sync-tool-XXX.zip -d target/release/
$ cd target/release
$ ./bin/rhq-sync-tool --help
...

How to build an RPM based on the release ?
==========================================

$ mvn clean assembly::assembly
$ export RPMBUILD=path/to/rpmbuild/
$ ./src/main/bash/make-rpm-tarball.sh
$ cd path/to/rpmbuild/
$ rpmbuild -bb ...

Note: RPMBUILD can be set to a remote server, using the SSH syntax, so, for instance:

export RPMBUILD=user@remote-builder:/path/to/remotedir/

TODO
====

* Urgent (for release):
** Release version 1.0 - JON 3.0 / RHQ 4.2.0 compatible
** Release version 1.1 - JON 3.1.0 / RHQ 4.3.0 compatible
* After release:
** Add proper static documentation (Awestruct ? Middleman ? MarkedDown ?)
** Make jon-mig.log location configurable
** Inspect dependencies, see if some more could be removed
** See if tools works for > 3.0.1
