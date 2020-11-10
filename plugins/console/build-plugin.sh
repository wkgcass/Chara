#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
find plugin/ -name 'Thumbs.db' -type f -delete
rm -rf plugin/code/
rm -f plugin/console.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/console.jar plugin/code/console.jar

cd plugin
zip --quiet --recurse-paths console.plugin plugin.json code
cd ../
