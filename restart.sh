#!/bin/bash

set -e

ssh root@web1.topobyte.de touch /opt/tomcat/tomcat-main/rootapps/bintray-checker/ROOT.war
