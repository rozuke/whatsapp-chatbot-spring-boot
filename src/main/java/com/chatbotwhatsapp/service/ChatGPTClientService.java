package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.chatgpt.MessageGPT;
import com.chatbotwhatsapp.model.chatgpt.requestChatGPT.RequestChatGPT;
import com.chatbotwhatsapp.service.interfaces.AIModelService;
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
public class ChatGPTClientService implements AIModelService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${chatgpt.api.token}")
    private String chatgptToken;

    @Value("${chatgpt.api.url}")
    private String chatgptAPIURL;

    private final String gptModel = "gpt-3.5-turbo";

    @Override
    public String getResponseFromAIModel(String context) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatgptToken);
        RequestChatGPT request = formatChatGPTRequest(context);
        HttpEntity<RequestChatGPT> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(chatgptAPIURL, entity, String.class);

        String textContext = response.getBody();
        if (textContext != null && !textContext.isEmpty()) {

            return  textContext;
        }
        return "";

    }

    private RequestChatGPT formatChatGPTRequest(String context) {
        return new RequestChatGPT(
                gptModel,
                new ArrayList<>(List.of(new MessageGPT("user", context))),
                0.7
        );
    }
}
