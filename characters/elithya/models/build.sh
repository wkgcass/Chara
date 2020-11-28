#!/bin/bash

set -e

ls ./elithya-high-dpi
ls ./elithya-mini
ls ./shared

find . -name '.DS_Store' -type f -delete
find . -name 'Thumbs.db' -type f -delete

function clean_shared() {
  rm -rf shared/code
  mkdir shared/code
}

function clean_output() {
  rm -f "elithya-high-dpi.model"
  rm -f "elithya-mini.model"
}

function clean_model() {
  model="$1"
  rm -f "$model/model.json"
  rm -f "$model/icon.png"
  rm -rf "$model/code/"
  rm -rf "$model/words/"
  rm -f "$model/values/values.json"
}

function compile() {
  cd ../
  ./gradlew clean jar
  cd ./models
  cp ../build/libs/elithya.jar ./shared/code/elithya.jar
}

function copy_shared() {
  model="$1"
  cp shared/model.json "$model/model.json"
  cp shared/icon.png "$model/icon.png"
  cp -r shared/code/ "$model/code/"
  cp -r shared/words/ "$model/words/"
  cp shared/values/values.json "$model/values/values.json"
}

function build_model() {
  model="$1"
  cd "$model/"
  zip --quiet --recurse-paths "$model.model" model.json icon.png code values words elithya
  cd ../
  mv "$model/$model.model" ./$model.model
}

clean_shared
clean_output
clean_model "elithya-high-dpi"
clean_model "elithya-mini"

compile
copy_shared "elithya-high-dpi"
copy_shared "elithya-mini"

build_model "elithya-high-dpi"
build_model "elithya-mini"

clean_shared
clean_model "elithya-high-dpi"
clean_model "elithya-mini"
