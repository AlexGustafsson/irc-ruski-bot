FROM openjdk:8-alpine

RUN mkdir -p /app
COPY build/irc-ruski-bot/ /app

WORKDIR /app

CMD ["./bin/irc-ruski-bot"]
