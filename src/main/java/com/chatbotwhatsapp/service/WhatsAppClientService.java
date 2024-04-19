package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.chatgpt.responseChatGPT.ResponseChatGPT;
import com.chatbotwhatsapp.model.whatsapp.requestMessage.RequestMessage;
import com.chatbotwhatsapp.model.whatsapp.requestMessage.TextResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ChatGPTClientService chatgptService;

    @Value("${whatsapp.webhook.url}")
    private String whatsappWebhookURL;
    @Value("${whatsapp.api.token}")
    private String bearerToken;
    @Value("${whatsapp.phone.number.test}")
    private String phoneNumber;

    public String sendPostRequestMessage(String message) {

        if (message != null && !message.isEmpty()) {

            String responseJson = chatgptService.getResponseFromAIModel(message);
            Gson gson = new Gson();
            ResponseChatGPT  responseChatGPT = gson.fromJson(responseJson, ResponseChatGPT.class);
            var responseContextBody =  responseChatGPT.getChoices().get(0).getMessage().getContent();

            if (!responseContextBody.isEmpty()) {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + bearerToken);
                RequestMessage messageRequest = formatMessage(responseContextBody);
                HttpEntity<RequestMessage> entity = new HttpEntity<>(messageRequest, headers);

                ResponseEntity<String> responseEntity = restTemplate.postForEntity(whatsappWebhookURL, entity, String.class);
                return responseEntity.getBody();
            }

        }
        return "";
    }
    
    private RequestMessage formatMessage (String message) {
        return new RequestMessage("whatsapp",
                "individual",
                phoneNumber,
                "text",
                new TextResponse(false, message)
        );
    }
}
