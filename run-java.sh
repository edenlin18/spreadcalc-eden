#!/bin/bash
set -o pipefail
set -e
set -x

JAVAC=javac
JAVA=java

#trap 'rm -f /tmp/test*.csv SpreadCalc SpreadCalc.class' EXIT

$JAVAC SpreadCalc.java

#$JAVA SpreadCalc < provided/testinput1.csv > /tmp/testoutput1.csv
#diff -u /tmp/testoutput1.csv provided/testoutput1.csv

$JAVA SpreadCalc  < provided/testinput2.csv > /tmp/testoutput2.csv -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1044
diff -u /tmp/testoutput2.csv provided/testoutput2.csv

$JAVA SpreadCalc < provided/testinput3.csv > /tmp/testoutput3.csv
diff -u /tmp/testoutput3.csv provided/testoutput3.csv

echo 'success!'

