#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
rm -rf plugin/code/
rm -f plugin/noto-font.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/noto-font.jar plugin/code/noto-font.jar

cd plugin
zip --quiet --recurse-paths noto-font.plugin plugin.json noto-font code
cd ../
