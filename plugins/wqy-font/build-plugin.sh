#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
rm -rf plugin/code/
rm -f plugin/wqy-font.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/wqy-font.jar plugin/code/wqy-font.jar

cd plugin
zip --quiet --recurse-paths wqy-font.plugin plugin.json wqy-font code wqy-microhei
cd ../
