#!/bin/bash
rm -rf ./javadoc/core
mkdir -p ./javadoc/core
cd ../core
./gradlew clean jar javadoc
cd ../
cp -r core/build/docs/javadoc/* ./docs/javadoc/core/
