#!/usr/bin/env bash

export LAUNCHER="io.vertx.core.Launcher"
export VERTICLE="com.apicatalog.eiger.service.TransformerVerticle"
export CMD="mvn compile"
export VERTX_CMD="run"
export PORT=8080

if [ "$1" == "dev" ]
  then
    mvn package dependency:copy-dependencies
    java \
      -cp  $(echo target/dependency/*.jar | tr ' ' ':'):"target/classes" \
      $LAUNCHER $VERTX_CMD $VERTICLE \
      --redeploy="src/main/java/**/*" \
      --on-redeploy="$CMD" \
      --redeploy-scan-period=1000 \
      --redeploy-grace-period=2500 \
      --launcher-class=$LAUNCHER

elif [ "$1" == "prod" ]
  then
    mvn clean package
    java -jar target/*-with-dependencies.jar

else
    echo "Please specify mode (dev, prod). e.g. 'start.sh dev'"
    exit 1
fi
