#!/bin/sh

for file in `find . -name "*.jar"`; do
  CLASSPATH=$file:$CLASSPATH
done

java -Djava.awt.headless=true -cp $CLASSPATH org.codefirst.shimbashishelf.cli.ShimbashiShelf $@
