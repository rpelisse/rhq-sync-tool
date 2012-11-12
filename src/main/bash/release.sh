#!/bin/sh
#
#

usage() {
  echo 'A simple script to build a release.'
  echo ''
  echo 'Following variable can be overriden using 'export':'
  echo 'RPMBUILDER_HOST: default value is 'rpmbuilder'.'
  echo 'RPMBUILDER_USER: default value is 'rpmbuilder'.'
  echo 'RPMBUILDER_DIR: default value is 'rpmbuild'.'
  echo ''
  echo 'NO_MVN: to prevent running maven - if already successfully ran'
}

readonly RPMBUILDER_HOST=${RPMBUILDER_HOST:-'rpmbuilder'}
readonly RPMBUILDER_USER=${RPMBUILDER_USER:-'rpmbuilder'}
readonly RPMBUILDER_DIR=${RPMBUILDER_DIR:-'rpmbuild'}

readonly VERSION=$(grep -e '<version>' pom.xml  | head -1 | sed -e 's/version//g' -e 's/[><]//g' -e 's;/;;' -e 's/ //g')
readonly DELIVERY_DIR="target/release-${VERSION}"
mkdir -p "${DELIVERY_DIR}"

if [ -z "${NO_MVN}" ]; then
  mvn -o clean assembly::assembly
fi
export RPMBUILD="${RPMBUILDER_USER}@${RPMBUILDER_HOST}:${RPMBUILDER_DIR}/SPECS/"
./src/main/bash/make-rpm-tarball.sh
ssh ${RPMBUILDER_USER}@${RPMBUILDER_HOST} "rpmbuild -bb ${RPMBUILDER_DIR}/SPECS/*${VERSION}*.spec"

scp "${RPMBUILDER_USER}@${RPMBUILDER_HOST}:${RPMBUILDER_DIR}/RPMS/noarch/*${VERSION}*.rpm" "${DELIVERY_DIR}"
cp target/*${VERSION}*.zip "${DELIVERY_DIR}"
