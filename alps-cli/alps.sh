#!/bin/bash

ALPS_JAR_NAME="alps-cli-0.2-SNAPSHOT.jar"

if [ -z "$ALPS_HOME" ]; then
  ALPS_HOME="."
fi

if [ -f $ALPS_HOME/alps-cli/target/$ALPS_JAR_NAME ]; then
  ALPS_JAR_PATH="$ALPS_HOME/alps-cli/target/$ALPS_JAR_NAME"

elif [ -f $ALPS_HOME/target/$ALPS_JAR_NAME ]; then
  ALPS_JAR_PATH="$ALPS_HOME/target/$ALPS_JAR_NAME"

elif [ -f $ALPS_HOME/$ALPS_JAR_NAME ]; then
  ALPS_JAR_PATH="$ALPS_HOME/$ALPS_JAR_NAME"

else 
  echo "$ALPS_JAR_NAME not found. Set ALPS_HOME variable pointing to alps directory."
  exit 1
fi

java -cp $ALPS_JAR_PATH com.apicatalog.alps.Command $1 $2 $3 $4
