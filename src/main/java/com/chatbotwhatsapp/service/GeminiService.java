package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.gemini.RequestGemini;
import com.chatbotwhatsapp.model.gemini.requestGemini.Content;
import com.chatbotwhatsapp.model.gemini.requestGemini.Part;
import com.chatbotwhatsapp.service.interfaces.AIModelService;
import com.chatbotwhatsapp.util.ChatBotConstant;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiService implements AIModelService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.url}")
    private String geminiURL;

    @Value("${gemini.api.key}")
    private String key;

    @Override
    public String getResponseFromAIModel(String context) {
        String formatURL = geminiURL + key;
        System.out.println("**********KEY***********");
        System.out.println(formatURL);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestGemini requestGemini = formatRequestFromGemini(context);
        HttpEntity<RequestGemini> entity = new HttpEntity<>(requestGemini, headers);
        ResponseEntity<String> request = restTemplate.postForEntity(formatURL, entity, String.class);
        String response = getResponseFromBody(JsonParser.parseString(request.getBody()));

        if (response != null && !response.isEmpty()) {
            return response;
        }

        return "";
    }

    private String getResponseFromBody(JsonElement jsonElement) {
        String response = "";

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray candidatesArray = jsonObject.getAsJsonArray("candidates");
            if (candidatesArray != null && !candidatesArray.isEmpty()) {
                JsonObject candidate = candidatesArray.get(0).getAsJsonObject();
                JsonObject content = candidate.getAsJsonObject("content");
                JsonArray partsArray = content.getAsJsonArray("parts");
                if (partsArray != null && !partsArray.isEmpty()) {
                    JsonObject part = partsArray.get(0).getAsJsonObject();
                    response = part.getAsJsonPrimitive("text").getAsString();
                    return response;
                }
            }
        }
        return response;
    }

    private RequestGemini formatRequestFromGemini(String text) {
        return new RequestGemini(new ArrayList<>(List.of(
                new Content(new ArrayList<>(List.of(new Part(ChatBotConstant.PROMPT + text))))
        )));

    }
}
