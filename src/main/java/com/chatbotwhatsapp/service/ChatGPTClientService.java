package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.chatgpt.MessageGPT;
import com.chatbotwhatsapp.model.chatgpt.requestChatGPT.RequestChatGPT;
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
public class ChatGPTClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${chatgpt.api.token}")
    private String chatgptToken;

    @Value("${chatgpt.api.url}")
    private String chatgptAPIURL;

    public String sendRequestChatGPT(String context) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatgptToken);
        RequestChatGPT request = formatChatGPTRequest(context);
        HttpEntity<RequestChatGPT> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(chatgptAPIURL, entity, String.class);

        String textContext = response.getBody();
        System.out.println("Chatgpt response " + textContext);
        return  textContext;
    }

    private RequestChatGPT formatChatGPTRequest(String context) {
        return new RequestChatGPT(
                "gpt-3.5-turbo",
                new ArrayList<>(List.of(new MessageGPT("user", context))),
                0.7
        );
    }
}
