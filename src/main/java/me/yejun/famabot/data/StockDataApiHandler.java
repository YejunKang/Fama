package me.yejun.famabot.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import me.yejun.famabot.Main;
import me.yejun.famabot.data.news.NewsData;
import me.yejun.famabot.data.quote.QuoteData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StockDataApiHandler {

    private static final String BASE_URL = "https://finnhub.io/api/v1/quote?symbol=";
    public static QuoteData fetchQuote(String symbol) {
        String urlString = BASE_URL + symbol + "&token=" + Main.API_TOKEN;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                return gson.fromJson(response.toString(), (Type) QuoteData.class);
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("API Call Failed");
    }

    public static String fetchCompanyNews(String symbol, String from, String to) throws IOException {
        URL url = new URL("https://finnhub.io/api/v1/company-news?symbol=" + symbol + "&from=" + from +"&to=" + to +"&token=" + Main.API_TOKEN);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        try (java.util.Scanner scanner = new java.util.Scanner(conn.getInputStream()).useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
