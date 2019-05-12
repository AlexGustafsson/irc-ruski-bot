package se.axgn.ircruskibot;

import java.io.IOException;

import java.text.MessageFormat;

import java.util.Map;

/**
* The main application.
*/
public class App {

  /**
  * The main entrypoint of the app.
  * @param args Command line parameters given to the app.
  */
  public static void main(String[] args) {
    Map<String, String> env = System.getenv();
    String server = env.getOrDefault("IRC_SERVER", null);
    int port = Integer.parseInt(env.getOrDefault("IRC_PORT", "6697"));
    String channel = env.getOrDefault("IRC_CHANNEL", "#random");
    String nick = env.getOrDefault("IRC_NICK", "ruski-bot");
    String user = env.getOrDefault("IRC_USER", "ruski-bot");
    String gecos = env.getOrDefault("IRC_GECOS", "Emoji Ruski v0.0.1 (github.com/AlexGustafsson/irc-ruski-bot)");

    if (server == null) {
      Log.error("Cannot start the bot without a given server");
    }

    IRC client = new IRC();
    try {
      Log.debug("Connecting to {0}:{1,number,#} as {2} ({3})", server, port, user, nick);
      client.connect(server, port, user, nick, gecos);
    } catch (IOException exception) {
      Log.error("Could not connect to the server", exception);
    }
    Log.debug("Connected");

    Log.debug("Joining {0}", channel);
    client.join(channel);

    Thread clientThread = new Thread(client);
    clientThread.start();

    Log.debug("Starting event loop");
    while (true) {
      try {
        Message message = client.retrieveMessage();
        if (message.body.equals(MessageFormat.format("{0}: help", nick))) {
          handleHelp(client, channel, nick);
        } else if (message.body.indexOf(MessageFormat.format("{0}: ", nick)) == 0) {
          handleTranslation(client, channel, message.body);
        }
      } catch (InterruptedException exception) {

      }
    }
  }

  private static void handleHelp(IRC client, String channel, String nick) {
    client.send(channel, "I translate your messages to Ruski. Example:");
    client.send(channel, MessageFormat.format("{0}: I only drive Lada -> YA yezzhu tol'ko na Lade", nick));
    client.send(channel, "You can also use the following command:");
    client.send(channel, MessageFormat.format("{0}: help -> this help text", nick));
  }

  private static void handleTranslation(IRC client, String channel, String body) {
    client.send(channel, body);
  }
}
