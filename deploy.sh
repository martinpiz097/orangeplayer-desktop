#!/bin/bash
mvn clean package
rm OrangePlayer\ Desktop.jar
mv targat/OrangePlayer\ Desktop.jar
rm -r target/
git add .
git commit -m "$1 $2 $3 $4"
git push gitlab master
git push github master