#!/bin/bash
#title           :startup.sh
#description     :Shell script to run Twitter Swiss Army Knife Project.
#author		 	 :orbit software solutions
#date            :2015-11-10
#version         :1.0    
#usage		 	 :bash startup.sh
#==============================================================================
java -Xmx128m -jar ./target/tsak-v1.0-jar-with-dependencies.jar $*