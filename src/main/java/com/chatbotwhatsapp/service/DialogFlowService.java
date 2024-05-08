package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.DialogflowMessage;
import com.chatbotwhatsapp.util.ChatBotConstant;
import com.chatbotwhatsapp.util.ContentType;
import com.google.cloud.dialogflow.cx.v3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

@Service
public class DialogFlowService {


    @Value("${dialogflow.api.project-id}")
    private String projectId;

    @Value("${dialogflow.api.agent-id}")
    private String agentId;

    @Value("${dialogflow.api.region-id}")
    private String regionId;

    private final UUID sessionId = UUID.randomUUID();
    private final String API_DIRECTION = "-dialogflow.googleapis.com:443";

    public List<DialogflowMessage> getResponseMessageProcessed(String intentMessage) {
        try {
                SessionsSettings.Builder sessionsSettings = SessionsSettings
                        .newBuilder()
                        .setEndpoint(regionId + API_DIRECTION);

                try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings.build())) {
                    DetectIntentResponse detectIntentResponse = getIntentResponseFromDialogflow(intentMessage, sessionId.toString(),sessionsClient);
                    return getMessages(detectIntentResponse);
                }
            } catch (IOException e) {
                return List.of();
            }
    }

    private List<DialogflowMessage> getMessages(DetectIntentResponse detectIntentResponse) {
        List<DialogflowMessage> messages = new ArrayList<>();
        System.out.println("*********Intent**********");
        System.out.println(detectIntentResponse.getQueryResult().getCurrentPage().getDisplayName());
        if (existMessages(detectIntentResponse)) {
            for (ResponseMessage message: detectIntentResponse.getQueryResult().getResponseMessagesList()) {

                if (!message.getText().getTextList().isEmpty()) {
                    String text = message.getText().getText(0);
                    if (isURL(text)) {
                        messages.add(new DialogflowMessage(ContentType.URL, text));
                    } else {
                        messages.add(new DialogflowMessage(ContentType.TEXT, text));
                    }
                }
            }
        }
        return messages;
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

}
