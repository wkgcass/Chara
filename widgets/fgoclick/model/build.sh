#!/bin/bash

set -e

ls ./model-src

find . -name '.DS_Store' -type f -delete
find . -name 'Thumbs.db' -type f -delete

function clean_output() {
  rm -f fgoclick.model
  rm -rf ./model-src/code
  mkdir ./model-src/code
}

function compile() {
  cd ../
  ./gradlew clean jar
  cd ./model
  cp ../build/libs/fgoclick.jar ./model-src/code/fgoclick.jar
}

function build_model() {
  cd model-src/
  zip --quiet --recurse-paths fgoclick.model model.json icon.png code values fgoclick
  cd ../
  mv model-src/fgoclick.model ./fgoclick.model
}

clean_output
compile
build_model
