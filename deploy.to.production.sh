#!/bin/bash

set -e

./gradlew clean war

rsync -av web/build/libs/web-template-basic-web-0.0.1-production.war \
root@web1.topobyte.de:/opt/tomcat/tomcat-secure/rootapps/changeme/ROOT.war
