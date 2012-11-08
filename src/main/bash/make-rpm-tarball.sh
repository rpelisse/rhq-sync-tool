#!/bin/sh
readonly TARGET_DIR='target'

if [ ! -e "${TARGET_DIR}" ]; then
    echo "Run 'mvn package' before this script."
fi
readonly ARTIFACT_ID=$(grep -e '<artifactId>' pom.xml | head -1 | sed -e 's;</*artifactId>;;g' | sed -e 's; ;;g')
readonly jar_suffix=$(ls ${TARGET_DIR}/*.jar | sed -e "s;^${TARGET_DIR}/;;" -e "s/${ARTIFACT_ID}-//" )
readonly VERSION=${jar_suffix%.jar}

readonly TARBALL_DIRNAME="${ARTIFACT_ID}-${VERSION}"
readonly TARBALL_DIR="${TARGET_DIR}/${TARBALL_DIRNAME}"
readonly TARBALL="${TARBALL_DIRNAME}.tgz"
readonly SPEC_FILE="${ARTIFACT_ID}-${VERSION}.spec"


mkdir  -p "${TARBALL_DIR}"
cp ${TARGET_DIR}/*.jar "${TARBALL_DIR}"
cp "${TARGET_DIR}/classes/${ARTIFACT_ID}" "${TARBALL_DIR}"
cp "${TARGET_DIR}/classes/${ARTIFACT_ID}.spec" "${SPEC_FILE}"
cd "${TARGET_DIR}"
tar cvzf "${TARBALL}" "${TARBALL_DIRNAME}"

if [ -n "${RPMBUILDER}" ]; then
  scp "${TARBALL}" "${RPMBUILDER}/SOURCES"
  cd -
  scp "${SPEC_FILE}" "${RPMBUILDER}/SPECS/"
fi
