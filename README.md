# IRC Emoji Bot
### A Dockerized IRC Ruski Bot written in Java 8
***

### Setting up

##### Quickstart

```Bash
# Clone the repository
git clone https://github.com/AlexGustafsson/irc-ruski-bot
# Enter the directory
cd irc-ruski-bot
# Build the image
./build-docker.sh
# Start the image
docker run -d -e IRC_SERVER='irc.example.org' --restart always axgn/irc-ruski-bot
```

### Documentation

#### Running with Docker

```Bash
docker run -d \
-e IRC_SERVER='irc.example.org' \
-e IRC_PORT='6697' \
-e IRC_CHANNEL='#random' \
-e IRC_NICK='emoji-bot' \
-e IRC_USER='emoji-bot' \
-e IRC_GECOS='Ruski Bot v0.1 (github.com/AlexGustafsson/irc-ruski-bot)' \
axgn/irc-ruski-bot
```

#### Invoking via IRC

To see help messages send `ruski-bot: help` in the channel where the bot lives.

To send a message translated to Ruski, send `ruski-bot: I only drive Lada` which will send `YA yezzhu tol'ko na Lade`.

If you have a secret (such as `I don't like vodka`) which you would like to share, you can send it in a direct message to the bot. It will still send the response to the specified channel without any mentioning of the sender.

### Contributing

Any contribution is welcome. If you're not able to code it yourself, perhaps someone else is - so post an issue if there's anything on your mind.

###### Development

```
# Clone the repository
git clone https://github.com/AlexGustafsson/irc-ruski-bot && cd irc-ruski-bot

# Compile and package the project
./gradlew assemble
# Compile, test and package the project
./gradlew build

# Run the project on port 3000
./gradlew run --args=3000
```

### Disclaimer

_Although the project is very capable, it is not built with production in mind. Therefore there might be complications when trying to use the bot for large-scale projects meant for the public. The bot was created to easily send Ruski in IRC channels and as such it might not promote best practices nor be performant._
