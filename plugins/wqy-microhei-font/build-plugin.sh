#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
rm -rf plugin/code/
rm -f plugin/wqy-microhei-font.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/wqy-microhei-font.jar plugin/code/wqy-microhei-font.jar

cd plugin
zip --quiet --recurse-paths wqy-microhei-font.plugin plugin.json wqy-microhei-font code
cd ../
