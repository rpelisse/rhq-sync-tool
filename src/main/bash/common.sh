readonly RHQ_CLIENT_JAVA_OPTS="${JAVA_OPTS} -Xms63m -Xmx128m -Djava.net.preferIPv4Stack=true"
readonly AGENT_PLATFORM=$(hostname -f)

setUpClassPath() {
  local jar_directory="${1}"

  local classpath="${BASEDIR}:${BASEDIR}/${project.name}-${project.version}.jar"
  for jarfile in $(ls ${jar_directory}/*.jar)
  do
    classpath="${classpath}:${jarfile}"
  done
  logDebug "Classpath defined:${classpath}"
  readonly CLASSPATH="${classpath}"
}

log() {
  local mssg="${1}"
  echo "${mssg}"
}

logDebug() {
  local mssg="${1}"

  if [ -z "${NODEBUG}" ]; then
    echo "${mssg}"
  fi
}

if [ -z "${DEBUG}" ] ; then
    readonly NODEBUG=true
fi

set -e
