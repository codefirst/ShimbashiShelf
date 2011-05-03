#!/bin/sh

for file in `find . -name "*.jar"`; do
  CLASSPATH=$file:$CLASSPATH
done

java -cp $CLASSPATH org.codefirst.shimbashishelf.ShimbashiShelf $@
