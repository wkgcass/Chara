#!/bin/bash

set -e

ls ./kokori-high-dpi
ls ./kokori-mini
ls ./shared

find . -name '.DS_Store' -type f -delete
find . -name 'Thumbs.db' -type f -delete

function clean_shared() {
  rm -rf shared/code
  mkdir shared/code
}

function clean_output() {
  rm -f "kokori-high-dpi.model"
  rm -f "kokori-mini.model"
}

function clean_model() {
  model="$1"
  rm -f "$model/model.json"
  rm -f "$model/icon.png"
  rm -rf "$model/code/"
  rm -rf "$model/words/"
  rm -f "$model/values/values.json"
  rm -f "$model/values/r18values.json"
}

function compile() {
  cd ../
  ./gradlew clean jar
  cd ./models
  cp ../build/libs/kokori.jar ./shared/code/kokori.jar
}

function copy_shared() {
  model="$1"
  cp shared/model.json "$model/model.json"
  cp shared/icon.png "$model/icon.png"
  cp -r shared/code/ "$model/code/"
  cp -r shared/words/ "$model/words/"
  cp shared/values/values.json "$model/values/values.json"
  cp shared/values/r18values.json "$model/values/r18values.json"
}

function build_model() {
  model="$1"
  cd "$model/"
  zip --quiet --recurse-paths "$model.model" model.json icon.png code values words kokori
  cd ../
  mv "$model/$model.model" ./$model.model
}

clean_shared
clean_output
clean_model "kokori-high-dpi"
clean_model "kokori-mini"

compile
copy_shared "kokori-high-dpi"
copy_shared "kokori-mini"

build_model "kokori-high-dpi"
build_model "kokori-mini"

clean_shared
clean_model "kokori-high-dpi"
clean_model "kokori-mini"
