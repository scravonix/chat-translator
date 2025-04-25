package com.chat.translator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

public class DeepLApi {
    private static final String FREE_ENDPOINT = "https://api-free.deepl.com/v2/translate";
    private static final String PRO_ENDPOINT = "https://api.deepl.com/v2/translate";

    public static String translate(String text, String sourceLang, String targetLang, String apiKey, boolean isPro) throws Exception {
        String endpoint = isPro ? PRO_ENDPOINT : FREE_ENDPOINT;
        HttpClient client = HttpClient.newHttpClient();
        JsonObject requestBody = new JsonObject();
        JsonArray textArray = new JsonArray();
        textArray.add(text);
        requestBody.add("text", textArray);
        if (sourceLang != null && !sourceLang.isEmpty()) {
            requestBody.addProperty("source_lang", sourceLang.toUpperCase());
        }
        requestBody.addProperty("target_lang", targetLang.toUpperCase());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "DeepL-Auth-Key " + apiKey.trim())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Detailed logging
        System.out.println("=== DeepL API Call Details ===");
        System.out.println("Target Endpoint: " + endpoint);
        System.out.println("Used API Key: " + apiKey);
        System.out.println("Request Headers: " + request.headers().map());
        System.out.println("Sent JSON Data: " + requestBody.toString());
        System.out.println("HTTP Method: " + request.method());
        System.out.println("==============================");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Response logging
        System.out.println("Response Status: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        System.out.println("Response Headers: " + response.headers().map());
        System.out.println("==============================");

        if (response.statusCode() == 200) {
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            if (json.has("translations") && json.getAsJsonArray("translations").size() > 0) {
                return json.getAsJsonArray("translations").get(0).getAsJsonObject().get("text").getAsString();
            } else {
                throw new Exception("API yanıtı çeviri içermiyor: " + response.body());
            }
        } else {
            String errorMessage = "API isteği " + response.statusCode() + " durumuyla başarısız oldu";
            if (response.body() != null && !response.body().isEmpty()) {
                errorMessage += ": " + response.body();
            }
            throw new Exception(errorMessage);
        }
    }
}