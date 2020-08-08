#!/bin/bash

ALPS_JAR_NAME="alps-cli-*.jar"

if [ -z "$ALPS_HOME" ]; then
  echo "ALPS_HOME is not set. Please, set ALPS_HOME variable."
  exit 1
fi

if [ -f $ALPS_HOME/alps-cli/target/$ALPS_JAR_NAME ]; then
  ALPS_JAR_PATH="$ALPS_HOME/alps-cli/target/$ALPS_JAR_NAME"

elif [ -f $ALPS_HOME/target/$ALPS_JAR_NAME ]; then
  ALPS_JAR_PATH="$ALPS_HOME/target/$ALPS_JAR_NAME"

elif [ -f $ALPS_HOME/$ALPS_JAR_NAME ]; then
  ALPS_JAR_PATH="$ALPS_HOME/$ALPS_JAR_NAME"

else 
  echo "$ALPS_JAR_NAME not found. Does ALPS_HOME pointing path to alps directory?"
  exit 1
fi

java -cp $ALPS_JAR_PATH com.apicatalog.alps.Command $1 $2 $3 $4
