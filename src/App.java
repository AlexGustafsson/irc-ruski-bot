package se.axgn.ircruskibot;

import java.io.IOException;
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
        String message = client.retrieveMessage();
        Log.debug("Got message: <{0}>", message);
      } catch (InterruptedException exception) {

      }
    }
  }
}
