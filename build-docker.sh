#!/usr/bin/env bash

set -e

mkdir -p build/irc-ruski-bot

./gradlew build

dist="$(ls build/distributions | grep 'tar' | sort | tail -1)"
tar -xf "build/distributions/$dist" -C build/irc-ruski-bot --strip-components=1

docker build -t axgn/irc-ruski-bot:latest .
