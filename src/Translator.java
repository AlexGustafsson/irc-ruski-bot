package se.axgn.ircruskibot;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.StandardCharsets;

import java.util.stream.Collectors;

import org.apache.http.client.utils.URIBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
* A class to translate between languages.
*/
public class Translator {
  /** The Yandex Translation API key. */
  String key;

  /**
  * Create an instance of a translator.
  * @param key The free Yandex Translation API key.
  */
  public Translator(String key) {
    this.key = key;
  }

  /**
  * Translate a message.
  * @param message The message to translate.
  * @param language The target language.
  */
  @SuppressFBWarnings(
      value = "CNT_ROUGH_CONSTANT_VALUE",
      justification = "The protocol, host nor path can be changed by a user."
  )
  public String translate(String message, String language) throws Exception, IOException, MalformedURLException, JSONException {
    // https://translate.yandex.net/api/v1.5/tr.json/translate?key=$YANDEX_TRANSLATE_API_KEY&text=hello%20world&lang=ru

    URIBuilder url = new URIBuilder();
    url.setScheme("https");
    url.setHost("translate.yandex.net");
    url.setPath("api/v1.5/tr.json/translate");
    url.addParameter("key", this.key);
    url.addParameter("text", message);
    url.addParameter("lang", language);

    URLConnection connection = new URL(url.toString()).openConnection();

    try (
        // Get the response
        InputStreamReader response = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader responseReader = new BufferedReader(response);
    ) {
      // Save the response as a string
      String responseString = responseReader.lines().collect(Collectors.joining("\n"));

      JSONObject json = new JSONObject(responseString);

      int responseCode = json.getInt("code");
      if (responseCode != 200) {
        throw new Exception("Got non-OK error code. Verify the request and API key");
      }

      JSONArray translations = json.getJSONArray("text");

      if (translations.length() > 0) {
        String translation = translations.getString(0);

        return translation;
      }
    } catch (IOException exception) {
      throw exception;
    }

    return null;
  }
}
