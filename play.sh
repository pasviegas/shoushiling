#!/usr/bin/env bash

if [ ! -f target/scala-2.11/shoushiling.jar ]
then
    activator assembly
fi

java -jar target/scala-2.11/shoushiling.jar