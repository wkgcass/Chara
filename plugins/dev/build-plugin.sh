#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
find plugin/ -name 'Thumbs.db' -type f -delete
rm -rf plugin/code/
rm -f plugin/dev.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/dev.jar plugin/code/dev.jar

cd plugin
zip --quiet --recurse-paths dev.plugin plugin.json code
cd ../
