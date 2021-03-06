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
docker run -d -e IRC_SERVER='irc.example.org' -e YANDEX_TRANSLATE_API_KEY='api key' --restart always axgn/irc-ruski-bot
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
-e YANDEX_TRANSLATE_API_KEY='api key' \
-e DISABLE_TLS_VALIDATION='false' \
axgn/irc-ruski-bot
```

The image is stateless and based on Alpine and is roughly 110MB in size. While running, the container usually uses 0.10% of the CPU and roughly 40MB of RAM. During load it uses about 15% CPU and while starting about 50% on a single core and an unchanged amout of RAM.

To prevent any unforseen events, one can therefore limit the container's resources by using the flags `--cpus=0.2` and `--memory=50MB` which should both leave some head room. Note that the lower CPU count will cause somewhat slower startups.

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

##
# Using Gradle

# If you're on a Windows PC, you may have to use ./gradlew.bat instead.

# --rerun-tasks ignore if the source code has seen no changes
# it ensures that you get the full result

# Run all checks and tests on the project
./gradlew check
# Test the project
./gradlew test
# Check code style
./gradlew --rerun-tasks checkstyleMain checkstyleTest
# Check for common bugs
./gradlew --rerun-tasks spotbugsMain spotbugsTest
# Check for dependency issues
./gradlew --rerun-tasks dependencyCheckAnalyze

# Compile and package the project
./gradlew assemble
# Compile, test and package the project
./gradlew build

# Run the project on port 3000
./gradlew run --args=3000
```

### Disclaimer

_Although the project is very capable, it is not built with production in mind. Therefore there might be complications when trying to use the bot for large-scale projects meant for the public. The bot was created to easily send Ruski in IRC channels and as such it might not promote best practices nor be performant._
