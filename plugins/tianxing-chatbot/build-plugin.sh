#!/bin/bash

set -e

ls plugin

find plugin/ -name '.DS_Store' -type f -delete
rm -rf plugin/code/
rm -f plugin/tianxing-chatbot.plugin
mkdir plugin/code/

./gradlew clean jar
cp build/libs/tianxing-chatbot.jar plugin/code/tianxing-chatbot.jar

cd plugin
zip --quiet --recurse-paths tianxing-chatbot.plugin plugin.json code
cd ../
