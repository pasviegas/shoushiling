#!/usr/bin/env bash

if [ ! -f target/scala-2.11/shoushiling_2.11-0.2.0-runnable.jar ]
then
    ./activator assembly
fi
java -jar target/scala-2.11/shoushiling_2.11-0.2.0-runnable.jar $1