#!/bin/bash

set -e

./gradlew clean war

rsync -av web/build/libs/bintray-checker-server-0.0.1-production.war \
root@xian.mobanisto.de:/opt/tomcat/tomcat-main/rootapps/bintray-checker/ROOT.war
