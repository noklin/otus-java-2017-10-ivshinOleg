#!/bin/sh
MEMORY='-Xmx128m -Xms128m' 
echo 'Test SerialGC...'
java $MEMORY -XX:+UseSerialGC -cp . com.noklin.Launcher
echo 'Test  ParallelGC...'
java $MEMORY -XX:+UseParallelGC -cp . com.noklin.Launcher
echo 'Test  ConcMarkSweepGC...'
java $MEMORY -XX:+UseConcMarkSweepGC -cp . com.noklin.Launcher
echo 'Test  G1GC...'
java $MEMORY -XX:+UseG1GC -cp . com.noklin.Launcher 
read -n 1 -s -r -p "Press any key to continue"