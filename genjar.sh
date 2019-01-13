#!/bin/bash
mvn clean compile
mvn clean package
rm OrangePlayer\ Desktop.jar
mv target/OrangePlayer\ Desktop.jar .
