#!/bin/bash
sh genjar.sh
rm -r target/
git add .
git commit -m "$1 $2 $3 $4"
git push gitlab master
git push github master
