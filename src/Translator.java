package se.axgn.ircruskibot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

import java.nio.charset.StandardCharsets;

import java.util.stream.Collectors;

import org.apache.http.client.utils.URIBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Translator {
  String key;

  public Translator(String key) {
    this.key = key;
  }

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

    // Get the response
    InputStreamReader response = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
    BufferedReader responseReader = new BufferedReader(response);

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

    return null;
  }
}
