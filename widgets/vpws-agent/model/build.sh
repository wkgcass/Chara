#!/bin/bash

set -e

ls ./model-src

find . -name '.DS_Store' -type f -delete
find . -name 'Thumbs.db' -type f -delete

function clean_output() {
  rm -f vpws-agent.model
  rm -rf ./model-src/code
  mkdir ./model-src/code
}

function compile() {
  cd ../
  ./gradlew clean jar
  cd ./model
  cp ../build/libs/vpws-agent.jar ./model-src/code/vpws-agent.jar
}

function build_model() {
  cd model-src/
  zip --quiet --recurse-paths vpws-agent.model model.json icon.png code values vpws-agent
  cd ../
  mv model-src/vpws-agent.model ./vpws-agent.model
}

clean_output
compile
build_model
