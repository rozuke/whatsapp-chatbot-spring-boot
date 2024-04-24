package com.chatbotwhatsapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DialogFlowClient {


    @Autowired
    private GeminiClientService geminiClient;


    public String sendResponseFromWebhook(String request) {

        Gson gson = new GsonBuilder().create();
        JsonObject parsedRequest = gson.fromJson(request, JsonObject.class);
        String requestTag = parsedRequest.getAsJsonObject("fulfillmentInfo").getAsJsonPrimitive("tag").toString();
        String responseText = getResponseText(requestTag);
        JsonObject responseObject = JsonParser
                        .parseString(
                                "{ \"fulfillment_response\": { \"messages\": [ { \"text\": { \"text\": ["
                                        + responseText
                                        + "] } } ] } }")
                        .getAsJsonObject();

        return  responseObject.toString();
    }


    private String getResponseText(String requestTag)  {
        String secondIntent = "\"test\"";
        String responseText;

        if (requestTag.equals(secondIntent)) {
            responseText = geminiClient.getResponseFromAIModel("Cual es la ciudad mas grande de europa");
        } else {
            responseText = "\"Sorry I didn't get that\"";
        }
        return responseText;
    }


}
