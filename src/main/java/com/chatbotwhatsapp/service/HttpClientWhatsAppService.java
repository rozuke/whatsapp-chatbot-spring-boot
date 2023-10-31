package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.responseMessage.ResponseMessage;
import com.chatbotwhatsapp.model.responseMessage.TextResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpClientWhatsAppService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String whatsappWebhookURL = "";
    private final String bearerToken = "";

    public void sendPostRequestMessage(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);
        ResponseMessage messageRequest = formatMessage(message);
        HttpEntity<ResponseMessage> entity = new HttpEntity<>(messageRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(whatsappWebhookURL, entity, String.class);

        // Process the response as needed
        String response = responseEntity.getBody();
        System.out.println("Response: " + response);
    }
    
    private ResponseMessage formatMessage (String message) {
        return new ResponseMessage("whatsapp", "individual", "59175140975", "text", new TextResponse(false, message));
    }
}
