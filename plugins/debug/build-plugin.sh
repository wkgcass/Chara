#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
rm -rf plugin/code/
rm -f plugin/debug.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/debug.jar plugin/code/debug.jar

cd plugin
zip --quiet --recurse-paths debug.plugin plugin.json code
cd ../
