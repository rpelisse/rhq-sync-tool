#!/bin/sh
#
#

usage() {
  echo 'A simple script to build a release.'
  echo ''
  echo 'Following variable can be overriden using 'export':'
  echo ''
  echo 'RPMBUILDER_HOST: default value is 'rpmbuilder'.'
  echo 'RPMBUILDER_USER: default value is 'rpmbuilder'.'
  echo 'RPMBUILDER_DIR: default value is 'rpmbuild'.'
  echo ''
  echo 'NO_MVN: to skip running maven - if already successfully ran'
  echo 'NO_RPM: to skip buildin the RPM'
  echo 'NO_CLEAN: keeps target/ directory from previous build'
}

set -e

readonly RPMBUILDER_HOST=${RPMBUILDER_HOST:-'rpmbuilder'}
readonly RPMBUILDER_USER=${RPMBUILDER_USER:-'rpmbuilder'}
readonly RPMBUILDER_DIR=${RPMBUILDER_DIR:-'rpmbuild'}

readonly VERSION=$(grep -e '<version>' pom.xml  | head -1 | sed -e 's/version//g' -e 's/[><]//g' -e 's;/;;' -e 's/ //g')
readonly DELIVERY_DIR="target/release-${VERSION}/"
echo -n "Preparing release ${VERSION}... "

if [ -z "${NO_CLEAN}" ]; then
  mvn -o clean 2>&1 > /dev/null
fi

mkdir -p ${DELIVERY_DIR}

if [ -z "${NO_MVN}" ]; then
  mvn -o assembly::assembly 2>&1 > /dev/null
  cp target/*${VERSION}*.zip ${DELIVERY_DIR}
fi

if [ -z "${NO_RPM}" ]; then
  export RPMBUILD="${RPMBUILDER_USER}@${RPMBUILDER_HOST}:${RPMBUILDER_DIR}/SPECS/"
  ./src/main/bash/make-rpm-tarball.sh
  ssh ${RPMBUILDER_USER}@${RPMBUILDER_HOST} "rpmbuild -bb ${RPMBUILDER_DIR}/SPECS/*${VERSION}*.spec 2>&1 > /dev/null"
  scp "${RPMBUILDER_USER}@${RPMBUILDER_HOST}:${RPMBUILDER_DIR}/RPMS/noarch/*${VERSION}*.rpm" ${DELIVERY_DIR}
fi

echo "Done."
