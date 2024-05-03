package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.util.ChatBotReader;
import com.google.cloud.dialogflow.cx.v3.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class DialogFlowClient {


    @Autowired
    private GeminiClientService geminiClient;

    @Value("${dialogflow.api.project-id}")
    private String projectId;

    @Value("${dialogflow.api.agent-id}")
    private String agentId;

    @Value("${dialogflow.api.region-id}")
    private String regionId;

    private final String API_DIRECTION = "-dialogflow.googleapis.com:443";

    private final String PROMPT = ChatBotReader.getPrompt();


    public String sendResponseFromWebhook(String request) {

        Gson gson = new GsonBuilder().create();
        JsonObject parsedRequest = gson.fromJson(request, JsonObject.class);
        String requestTag = parsedRequest.getAsJsonObject("fulfillmentInfo").getAsJsonPrimitive("tag").toString();
        String responseText = getResponseFromGeminiAI(requestTag);
        JsonObject responseObject = JsonParser
                        .parseString(
                                "{ \"fulfillment_response\": { \"messages\": [ { \"text\": { \"text\": ["
                                        + responseText
                                        + "] } } ] } }")
                        .getAsJsonObject();

        return  responseObject.toString();
    }

    public String processMessageFromDialogFlow(String json) {
        String intentMessage = extractTextFromJSON(json);
        UUID sessionId;
        try {
                SessionsSettings.Builder sessionsSettings = SessionsSettings
                        .newBuilder()
                        .setEndpoint(regionId + API_DIRECTION);

                try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings.build())) {
                    sessionId = UUID.randomUUID();
                    DetectIntentResponse detectIntentResponse = getIntentResponseFromDialogflow(intentMessage, sessionId.toString(),sessionsClient);
                    return getProcessedMessageFromGeminiAI(detectIntentResponse);
                }
            } catch (IOException e) {
                return "Un momento por favor";
            }
    }

    private String getProcessedMessageFromGeminiAI(DetectIntentResponse detectIntentResponse) {
        if (existMessages(detectIntentResponse)) {
            for (ResponseMessage message: detectIntentResponse.getQueryResult().getResponseMessagesList()) {
                if (!message.getText().getTextList().isEmpty()) {
                    String dialogflowResponse = message.getText().getText(0);
                        String formatString = PROMPT + dialogflowResponse;
                        return geminiClient.getResponseFromAIModel(formatString);
                }
            }
        }
        return "";
    }

    private boolean existMessages(DetectIntentResponse detectIntentResponse) {
        return detectIntentResponse.getQueryResult().getResponseMessagesCount() > 0;
    }

    private DetectIntentResponse getIntentResponseFromDialogflow(String message, String sessionId, SessionsClient sessionsClient) {

        SessionName sessionName = SessionName.ofProjectLocationAgentSessionName(projectId, regionId, agentId, sessionId);
        QueryInput queryInput = QueryInput.newBuilder()
                .setText(
                        TextInput.newBuilder()
                                .setText(message)
                                .build()
                ).setLanguageCode("es")
                .build();

        DetectIntentRequest detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(sessionName.toString())
                .setQueryInput(queryInput)
                .build();

        return sessionsClient.detectIntent(detectIntentRequest);
    }

    private String extractTextFromJSON(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get("text").getAsString();
    }


    private String getResponseFromGeminiAI(String requestTag)  {
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
