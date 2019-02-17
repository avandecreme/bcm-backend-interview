package org.flybcm;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequestHelper {

  private static final OkHttpClient okHttpClient = new OkHttpClient();

  public static String makeStringHttpRequest(String url, String apiKey)
    throws IOException {

    Request request = new Request.Builder().url(url)
      .addHeader("X-API-Key", apiKey).build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      return response.body().string();
    }
  }
}
