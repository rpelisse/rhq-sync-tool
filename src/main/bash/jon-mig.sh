#!/bin/bash

readonly IMPORT_OPERATION_CLASSNAME="org.jboss.rhq.jon.mig.RhqSyncTool"
readonly BASEDIR="."

source "${BASEDIR}/bin/common.sh"

setUpClassPath "${BASEDIR}/lib"

if [ $# -eq 0 ] ;then
    java  -classpath "${CLASSPATH}" --help
else
    java ${RHQ_CLIENT_JAVA_OPTS} -classpath "${CLASSPATH}" "${IMPORT_OPERATION_CLASSNAME}" ${*}
fi
exit ${?}
