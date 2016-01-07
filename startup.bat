@echo off
:title           :startup.sh
:description     :Batch script to run Twitter Swiss Army Knife Project.
:author		 	 :orbit software solutions
:date            :2015-11-13
:version         :1.0    
:usage		 	 :startup.bat
:==============================================================================

java -Xmx128m -jar ./target/tsak-v1.0-jar-with-dependencies.jar %*