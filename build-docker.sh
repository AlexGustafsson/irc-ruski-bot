#!/usr/bin/env bash

set -e

./gradlew assemble

rm -rf build/irc-ruski-bot
mkdir -p build/irc-ruski-bot

dist="$(ls build/distributions | grep 'tar' | sort | tail -1)"
tar -xf "build/distributions/$dist" -C build/irc-ruski-bot --strip-components=1

docker build --no-cache -t axgn/irc-ruski-bot:latest .
