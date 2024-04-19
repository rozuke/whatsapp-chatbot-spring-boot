package com.chatbotwhatsapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

@Service
public class DialogFlowClient {


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

    private static String getResponseText(String requestTag) {
        String defaultIntent = "\"Default Welcome Intent\"";
        String secondIntent = "\"test\"";
        String responseText = "";

        if (requestTag.equals(defaultIntent)) {
            responseText = "\"Hello from a Java GCF Webhook\"";
        } else if (requestTag.equals(secondIntent)) {
            responseText = "\"My name is Flowhook\"";
        } else {
            responseText = "\"Sorry I didn't get that\"";
        }
        return responseText;
    }


}
