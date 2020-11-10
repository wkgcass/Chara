#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
find plugin/ -name 'Thumbs.db' -type f -delete
rm -rf plugin/code/
rm -f plugin/r18.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/r18.jar plugin/code/r18.jar

cd plugin
zip --quiet --recurse-paths r18.plugin plugin.json code
cd ../
