package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.whatsapp.requestMessage.RequestMessage;
import com.chatbotwhatsapp.model.whatsapp.requestMessage.TextResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResponseMessageService {

    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${whatsapp.webhook.url}")
    private String whatsappWebhookURL;
    @Value("${whatsapp.api.token}")
    private String bearerToken;
    @Value("${whatsapp.phone.number.test}")
    private String phoneNumber;

    public String sendPostRequestMessage(String message) {
        System.out.println("*******WhatsApp Message********");
        System.out.println(message);

        if (message != null && !message.isEmpty()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + bearerToken);
                RequestMessage messageRequest = formatMessage(message);
                HttpEntity<RequestMessage> entity = new HttpEntity<>(messageRequest, headers);

                ResponseEntity<String> responseEntity = restTemplate.postForEntity(whatsappWebhookURL, entity, String.class);
                return responseEntity.getBody();
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
