package se.axgn.ircruskibot;

/**
* Colors for use when writing text to an ANSI compatible terminal.
*/
public enum LogColor {
  Red("\u001B[31m"),
  Orange("\u001b[31;1m"),
  Green("\u001B[32m"),
  Blue("\u001b[34m"),
  Grey("\u001b[90m"),
  Default("\u001B[0m");

  private final String code;

  LogColor(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }
}
