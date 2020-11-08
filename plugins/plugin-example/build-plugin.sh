#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
rm -rf plugin/code/
rm -f plugin/plugin-example.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/plugin-example.jar plugin/code/plugin-example.jar

cd plugin
zip --quiet --recurse-paths plugin-example.plugin plugin.json plugin-example code
cd ../
