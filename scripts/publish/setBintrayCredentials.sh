#!/usr/bin/env bash
mkdir -p ~/.bintray
eval "echo \"$(< ./scritps/publish/bintray.template)\"" > ~/.bintray/.credentials