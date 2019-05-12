package se.axgn.ircruskibot;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
* A message retrieved from the IRC server.
*/
@SuppressFBWarnings(
    value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD",
    justification = "This class is used as a simple struct."
)
public class Message {
  /** The username of the message author. */
  public String sender;
  /** The IRC message type. */
  public String messageType;
  /** The target audience, such as the target channel. */
  public String target;
  /** The message's body. */
  public String body;

  /**
  * Instantiate a message.
  */
  public Message(String sender, String messageType, String target, String body) {
    this.sender = sender;
    this.messageType = messageType;
    this.target = target;
    this.body = body;
  }
}
