package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.util.ChatBotConstant;
import com.google.cloud.dialogflow.cx.v3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

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

    private UUID sessionId = UUID.randomUUID();
    private final String API_DIRECTION = "-dialogflow.googleapis.com:443";

    public List<String> getResponseMessageProcessed(String json) {
        String intentMessage = extractTextFromJSON(json);
        try {
                SessionsSettings.Builder sessionsSettings = SessionsSettings
                        .newBuilder()
                        .setEndpoint(regionId + API_DIRECTION);

                try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings.build())) {
                    DetectIntentResponse detectIntentResponse = getIntentResponseFromDialogflow(intentMessage, sessionId.toString(),sessionsClient);
                    return getMessages(detectIntentResponse);
                }
            } catch (IOException e) {
                return List.of("No se sobre eso");
            }
    }

    private List<String> getMessages(DetectIntentResponse detectIntentResponse) {
        List<String> processedMessages = new ArrayList<>();
        if (existMessages(detectIntentResponse)) {
            for (ResponseMessage message: detectIntentResponse.getQueryResult().getResponseMessagesList()) {
                if (!message.getText().getTextList().isEmpty()) {
                    String text = message.getText().getText(0);
                    if (isURL(text)) {
                        processedMessages.add(text);
                    } else {
                        String processedMessage = geminiClient.getResponseFromAIModel(text);
                        processedMessages.add(processedMessage);
                    }

                }
            }
        }
        return processedMessages;
    }

    private boolean isURL(String url){
        Matcher matcher = ChatBotConstant.URL_PATTERN.matcher(url);
        return matcher.matches();
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

}
