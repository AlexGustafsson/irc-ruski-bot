package se.axgn.ircruskibot;

import java.io.IOException;

import java.text.MessageFormat;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.Map;

/**
* The main application.
*/
public class App {
  static Translator translator;

  /**
  * The main entrypoint of the app.
  * @param args Command line parameters given to the app.
  */
  public static void main(String[] args) {
    final Map<String, String> env = System.getenv();
    final String server = env.getOrDefault("IRC_SERVER", null);
    final int port = Integer.parseInt(env.getOrDefault("IRC_PORT", "6697"));
    final String channel = env.getOrDefault("IRC_CHANNEL", "#random");
    final String nick = env.getOrDefault("IRC_NICK", "ruski-bot");
    final String user = env.getOrDefault("IRC_USER", "ruski-bot");
    final String gecos = env.getOrDefault("IRC_GECOS", "Emoji Ruski v0.0.1 (github.com/AlexGustafsson/irc-ruski-bot)");
    final String yandexTranslateKey = env.getOrDefault("YANDEX_TRANSLATE_API_KEY", null);
    final boolean disableTLSValidation = Boolean.parseBoolean(env.getOrDefault("DISABLE_TLS_VALIDATION", "false"));

    if (server == null) {
      Log.error("Cannot start the bot without a given server");
      System.exit(1);
    }

    if (yandexTranslateKey == null) {
      Log.error("Cannot start the bot without a free Yandex Translate API key");
      System.exit(1);
    }

    if (disableTLSValidation) {
      Log.warn("Disabling TLS validation. You are now susceptible to MITM attacks!");
    }

    translator = new Translator(yandexTranslateKey);

    IRC client = new IRC(disableTLSValidation);
    try {
      Log.debug("Connecting to {0}:{1,number,#} as {2} ({3})", server, port, user, nick);
      client.connect(server, port, user, nick, gecos);
    } catch (IOException exception) {
      Log.error("Could not connect to the server", exception);
    } catch (NoSuchAlgorithmException exception) {
      Log.error("Could not connect to the server", exception);
    } catch (KeyManagementException exception) {
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
          String body = message.body.substring(nick.length() + 2);
          handleTranslation(client, channel, body);
        }
      } catch (InterruptedException exception) {
        Log.debug("Interrupted during message retrieval", exception);
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
    try {
      String translation = translator.translate(body, "ru");
      if (translation != null) {
        client.send(channel, translation);
      }
    } catch (Exception exception) {
      Log.debug("Could not translate message", exception);
    }
  }
}
