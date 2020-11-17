#!/bin/bash

JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)

if [ -z "$JAVA_VERSION" ]; then
  echo "Java is not installed or java commnand in not accessible. Please install Java 11 or greater."
  exit 1
fi

if [ "$JAVA_VERSION" -lt 11 ]; then
  echo "alps.sh requires Java 11 or greater. Installed Java version is $JAVA_VERSION"
  exit 1
fi

ALPS_JAR_NAME="alps-cli-*.jar"

if [ -z "$ALPS_HOME" ]; then
  echo "ALPS_HOME is not set. Please, set ALPS_HOME variable."
  exit 1
fi

if [ -f $ALPS_HOME/$ALPS_JAR_NAME ]; then
  ALPS_PATH=$ALPS_HOME
else 
  echo "$ALPS_JAR_NAME not found. Does ALPS_HOME pointing path to alps directory?"
  exit 1
fi

java -cp $(echo $ALPS_PATH/$ALPS_JAR_NAME):$(echo $ALPS_PATH/lib/*.jar | tr ' ' ':') com.apicatalog.alps.cli.AlpsCommand $@
