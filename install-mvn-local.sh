#!/bin/bash

set -e
set -E

_work_dir=`dirname "$0"`

mvn install:install-file -Dfile="${_work_dir}/external-lib/ojdbc6/ojdbc6.jar" -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar -DcreateChecksum=true

mvn install:install-file -Dfile="${_work_dir}/external-lib/fadada/fadada-api-sdk/v20190513/fadada_api_sdk_v20190513.jar" -DgroupId=com.fadada -DartifactId=fadada-api-sdk -Dversion=v20190513 -Dpackaging=jar -DcreateChecksum=true
mvn install:install-file -Dfile="${_work_dir}/external-lib/fadada/fadada-api-sdk/v20190513/fadada_api_sdk_source_v20190513.jar" -DgroupId=com.fadada -DartifactId=fadada-api-sdk -Dversion=v20190513 -Dpackaging=jar -DcreateChecksum=true -Dclassifier=sources

exit 0
