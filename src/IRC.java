package se.axgn.ircruskibot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.text.MessageFormat;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
* An IRC connector.
*/
public class IRC implements Runnable {
  SSLSocket socket;
  PrintWriter outputWriter;
  BufferedReader inputReader;
  BlockingQueue messages = new ArrayBlockingQueue<Message>(1024);
  volatile boolean shouldRun;

  /**
  * Instantiate an IRC client.
  */
  public IRC() {

  }

  /**
  * Connect to an IRC server.
  * @param server The server to connect to.
  * @param port The server port to connect to.
  * @param user The IRC user to connect as.
  * @param nick The nickname to use.
  * @param gecos The realname (description) of the bot.
  */
  public void connect(String server, int port, String user, String nick, String gecos) throws IOException {
    SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    this.socket = (SSLSocket) factory.createSocket(server, port);
    this.socket.startHandshake();

    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream());
    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
    this.outputWriter = new PrintWriter(bufferedWriter);

    InputStreamReader inputStreamReader = new InputStreamReader(this.socket.getInputStream());
    this.inputReader = new BufferedReader(inputStreamReader);

    this.send(MessageFormat.format("USER {0} {0} {0} :{1}", user, gecos));
    this.send(MessageFormat.format("NICK {0}", nick));

    this.shouldRun = true;
  }

  /**
  * Disconnect from the server.
  */
  public void disconnect() throws IOException {
    this.shouldRun = false;
    this.outputWriter.close();
    this.inputReader.close();
    this.socket.close();
  }

  /**
  * Connect to a channel.
  */
  public void join(String channel) {
    this.send(MessageFormat.format("JOIN {0}", channel));
  }

  /**
  * Read a message from the server.
  */
  private String read() {
    String message;
    try {
      message = this.inputReader.readLine();
    } catch (IOException exception) {
      message = null;
    }

    return message;
  }

  /**
  * Send a command to the server.
  * @param command The command to send.
  */
  private void send(String command) {
    this.outputWriter.print(command);
    this.outputWriter.print("\r\n");
    this.outputWriter.flush();
  }

  /**
  * Send a message to a specific channel.
  * @param channel The channel to send the message to.
  * @param message The message to send.
  */
  public void send(String channel, String message) {
    this.send(MessageFormat.format("PRIVMSG {0} :{1}", channel, message));
  }

  /**
  * Get a queued message.
  */
  public Message retrieveMessage() throws InterruptedException {
    return (Message) this.messages.take();
  }

  private void pong(String key) {
    this.send(MessageFormat.format("PONG :{0}", key));
  }

  public void run() {
    while (this.shouldRun) {
      String message = this.read();
      if (message == null)
        continue;

      if (message.indexOf("PING") == 0) {
        String key = message.split(" ")[1];
        this.pong(key);
      } else {
        try {
          String[] parts = message.split(" ");
          String sender = parts[0].split("!")[0].substring(1);
          String messageType = parts[1];
          String target = parts[2];
          String body = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length)).substring(1);
          this.messages.put(new Message(sender, messageType, target, body));
        } catch (InterruptedException exception) {
          Log.debug("The message queue is full, dropping message");
        } catch (StringIndexOutOfBoundsException exception) {

        }
      }
    }
  }
}
