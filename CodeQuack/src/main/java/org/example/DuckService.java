package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject; // Mozda ces morati dodati org.json dependency, ili parsiraj rucno string

public class DuckService {

    // HACKATHON TIP: Za demo, stavi API key ovde.
    // Ali pazi da ne pushujes ovo na javni GitHub!
    private static final String API_KEY = "sk-proj-TVOJ-API-KEY-OVDE";
    private static final String URL = "https://api.openai.com/v1/chat/completions";

    public String askTheDuck(String userProblem) {
        try {
            // Rucno pravimo JSON string da izbegnemo dependencies ako zuris
            // Ali bolje je koristiti Gson ili Jackson biblioteku
            String jsonBody = "{"
                    + "\"model\": \"gpt-3.5-turbo\","
                    + "\"messages\": ["
                    + "  {\"role\": \"system\", \"content\": \"You are a rubber duck debugger. Ask clarifying questions only. Do not give solutions.\"},"
                    + "  {\"role\": \"user\", \"content\": \"" + escapeJson(userProblem) + "\"}"
                    + "]"
                    + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Ovde bi trebalo parsirati JSON response da izvuces samo tekst
            // Za hakaton, ako nemas parser, mozes koristiti prosti string manipulation
            return extractContentFromResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return "Kva kva! Došlo je do greške: " + e.getMessage();
        }
    }

    // Veoma prljava pomocna metoda za hakaton brzinu
    private String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    }

    // Jos prljavija metoda za izvlacenje odgovora bez JSON biblioteke
    private String extractContentFromResponse(String jsonResponse) {
        // U realnosti koristi Jackson ili Gson biblioteku!
        // Ovo je samo da proradi odmah:
        String marker = "\"content\": \"";
        int start = jsonResponse.indexOf(marker);
        if (start == -1) return jsonResponse; // Vrati sve ako ne nadje
        start += marker.length();
        int end = jsonResponse.indexOf("\"", start);
        String content = jsonResponse.substring(start, end);
        return content.replace("\\n", "\n").replace("\\\"", "\"");
    }
}
