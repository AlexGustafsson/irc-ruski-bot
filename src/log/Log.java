package se.axgn.ircruskibot;

import java.io.PrintStream;

import java.text.MessageFormat;

/**
* A public class to use for anything logging related.
* All functions use the message format.
* @see <a href="https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html">https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html</a>
*/
public class Log {
  static Long lastWriteTime = 0L;
  static PrintStream out;

  static {
    // Initiate the last write time when the class is first used
    lastWriteTime = System.currentTimeMillis();

    // Set the default output as stdout
    out = System.out;
  }

  /**
  * Set what output stream to use. Defaults to System.out.
  * @param newOut The output stream to use from here on
  */
  public static void setOut(PrintStream newOut) {
    out = newOut;
  }

  /**
  * Write a message out to stdout.
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void write(String format, Object... args) {
    Long time = System.currentTimeMillis();

    out.println(format(format, args));
    lastWriteTime = time;
  }

  /**
  * Write a log message.
  * @param label The label to use for the log message.
    Displayed like [lable] at the start of the string
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void log(String label, String format, Object... args) {
    Long time = System.currentTimeMillis();
    Long offset = time - lastWriteTime;
    // Create a string for the time difference like "+5ms"
    String offsetString = colorize("+" + formatTime(offset), LogColor.Grey);

    String output = format(format, args);

    write("{0} [{1}] {2}", offsetString, label, output);
  }

  /**
  * Write a log message with a stack trace.
  * @param label The label to use for the log message.
    Displayed like [lable] at the start of the string
  * @param stackTrace The stack trace to print
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static synchronized void log(String label, StackTraceElement[] stackTrace, String format, Object... args) {
    StackTraceElement caller = stackTrace[2];
    String fileName = caller.getFileName();
    String className = caller.getClassName();
    String methodName = caller.getMethodName();
    int lineNumber = caller.getLineNumber();

    log(label, colorize("{0}:{1} -> {2}@{3}", LogColor.Grey), fileName, lineNumber, methodName, className);
    write(colorize("    └─ " + format, LogColor.Grey), args);
  }

  /**
  * Write a log message with a Throwable.
  * @param label The label to use for the log message.
    Displayed like [lable] at the start of the string
  * @param t The Throwable to print
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static synchronized void log(String label, Throwable t, String format, Object... args) {
    StackTraceElement caller = t.getStackTrace()[2];
    String fileName = caller.getFileName();
    String className = caller.getClassName();
    String methodName = caller.getMethodName();
    int lineNumber = caller.getLineNumber();

    log(label, "{0}:{1} -> {2}@{3}", fileName, lineNumber, methodName, className);
    write("    ├─ " + format, args);
    write(formatThrowable(t));
  }

  /**
  * Write a trace message.
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void trace(String format, Object... args) {
    log(colorize("trace", LogColor.Blue), format, args);
  }

  /**
  * Write a trace message.
  * @param message The message to print
  * @param t The trace to use
  */
  public static void trace(String message, Throwable t) {
    log(colorize("trace", LogColor.Blue), t.getStackTrace(), "{0} --> {1}", t.toString(), message);
  }

  /**
  * Write a debug message.
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void debug(String format, Object... args) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    log(colorize("debug", LogColor.Blue), stackTrace, format, args);
  }

  /**
  * Write a debug message.
  * @param message The message to print
  * @param t The trace to use
  */
  public static void debug(String message, Throwable t) {
    log(colorize("debug", LogColor.Blue), t, "{0} --> {1}", t.toString(), message);
  }

  /**
  * Write an error message.
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void error(String format, Object... args) {
    log(colorize("error", LogColor.Red), format, args);
  }

  /**
  * Write an error message.
  * @param message The message to print
  * @param t The trace to use
  */
  public static void error(String message, Throwable t) {
    log(colorize("error", LogColor.Red), t, "{0} --> {1}", t.toString(), message);
  }

  /**
  * Write a warn message.
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void warn(String format, Object... args) {
    log(colorize("warn", LogColor.Orange), format, args);
  }

  /**
  * Write a warning message.
  * @param message The message to print
  * @param t The trace to use
  */
  public static void warn(String message, Throwable t) {
    log(colorize("warn", LogColor.Orange), t.getStackTrace(), "{0} --> {1}", t.toString(), message);
    write(formatThrowable(t));
  }

  /**
  * Write an info message.
  * @param format The format to use for printing
  * @param args The arguments to use with the format
  */
  public static void info(String format, Object... args) {
    log(colorize("info", LogColor.Green), format, args);
  }

  /**
  * Write an info message.
  * @param message The message to print
  * @param t The trace to use
  */
  public static void info(String message, Throwable t) {
    log(colorize("info", LogColor.Green), t.getStackTrace(), "{0} --> {1}", t.getMessage(), message);
  }

  /**
  * Colorize a string using ANSI escape codes.
  * @param output The string to colorize
  * @param color The LogColor to use when colorizing
  * @return Colorized string
  */
  public static String colorize(String output, LogColor color) {
    return color + output + LogColor.Default;
  }

  /**
  * Format a millisecond timestamp to human readable values.
  * Splits the timestamp into days, hours, minutes, seconds and milliseconds.
  * Only prints non-zero values, except for milliseconds which is always printed.
  * @param timestamp The number of milliseconds elapsed since a defined epoch
  * @return Readable time
  */
  public static String formatTime(Long timestamp) {
    final long milliseconds = timestamp % 1000;
    final long seconds = (timestamp / 1000) % 60;
    final long minutes = (timestamp / (1000 * 60)) % 60;
    final long hours = (timestamp / (1000 * 60 * 60)) % 24;
    final long days = (timestamp / (1000 * 60 * 60 * 24));

    String formattedTime = "";

    if (days > 0) {
      formattedTime += days + "d";
    }

    if (hours > 0) {
      formattedTime += hours + "h";
    }

    if (minutes > 0) {
      formattedTime += minutes + "m";
    }

    if (seconds > 0) {
      formattedTime += seconds + "s";
    }

    formattedTime += milliseconds + "ms";

    return formattedTime;
  }

  /**
  * Format a Throwable to match our output.
  * @param t The throwable to format
  * @return The formatted string
  */
  public static String formatThrowable(Throwable t) {
    StackTraceElement[] stackTrace = t.getStackTrace();
    StringBuffer result = new StringBuffer("    │\n");

    for (int i = stackTrace.length - 1; i >= 0; i--) {
      StackTraceElement current = stackTrace[i];
      String fileName = current.getFileName();
      String className = current.getClassName();
      String methodName = current.getMethodName();
      int lineNumber = current.getLineNumber();

      result.append(format("    │ {0}:{1} -> {2}@{3}\n", fileName, lineNumber, methodName, className));

      // Only include the first non-dq trace
      if (!className.contains("se.dequeue")) {
        result.append("    │ ...\n    │ ");
        result.append(t.toString());
        result.append("\n    └──");
        break;
      }
    }

    return result.toString();
  }

  /**
  * Format a message using the message format.
  * Messages are of type "Hello {}" with args like "world".
  * @param format The format
  * @param args The arguments replacing {}
  * @return The formatted string
  */
  public static String format(String format, Object... args) {
    MessageFormat formatter = new MessageFormat(format);
    return formatter.format(args);
  }
}
