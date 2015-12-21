#!/usr/bin/env bash
mkdir -p ~/.bintray
eval "echo \"$(< ./scripts/publish/credentials.template)\"" > ~/.bintray/.credentials
if [ -f ~/.bintray/.credentials ]
then
    echo "~/.bintray/.credentials created"
fi
