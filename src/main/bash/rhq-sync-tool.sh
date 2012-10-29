#!/bin/bash

readonly MAIN_CLASSNAME="org.jboss.rhq.sync.tool.RhqSyncTool"
readonly BASEDIR="."

source "${BASEDIR}/bin/common.sh"

setUpClassPath "${BASEDIR}/lib"

if [ $# -eq 0 ] ;then
    java  -classpath "${CLASSPATH}"  "${MAIN_CLASSNAME}" '--help'
else
    java ${RHQ_CLIENT_JAVA_OPTS} -classpath "${CLASSPATH}" "${MAIN_CLASSNAME}" ${*}
fi
exit ${?}
