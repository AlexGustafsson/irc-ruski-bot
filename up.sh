#!/usr/bin/env bash

# Please see the README for more information
docker run \
  --env IRC_SERVER='irc.example.org' \
  --env IRC_PORT='6697' \
  --env IRC_CHANNEL='#random' \
  --env IRC_NICK='ruski-bot' \
  --env IRC_USER='ruski-bot' \
  --env IRC_GECOS='Ruski Bot v0.1 (github.com/AlexGustafsson/irc-ruski-bot)' \
  --env YANDEX_TRANSLATE_API_KEY="$YANDEX_TRANSLATE_API_KEY" \
  --name irc-ruski-bot \
  --detach \
  --restart always \
  --cpus=0.2 \
  --memory=50MB \
  axgn/irc-ruski-bot
