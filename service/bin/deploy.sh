#!/usr/bin/env bash

gcloud app deploy target/*-jar-with-dependencies.jar --appyaml=src/appengine/app.yaml
