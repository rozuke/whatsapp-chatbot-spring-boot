package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.whatsapp.requestMessage.RequestMessage;
import com.chatbotwhatsapp.model.whatsapp.requestMessage.TextResponse;
import com.chatbotwhatsapp.model.whatsapp.responseMessage.Message;
import com.chatbotwhatsapp.model.whatsapp.responseMessage.ResponseMessage;
import com.chatbotwhatsapp.persistence.crud.UserRepository;
import com.chatbotwhatsapp.persistence.entity.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ResponseMessageService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DialogFlowService dialogFlowService;


    @Value("${whatsapp.webhook.url}")
    private String whatsappWebhookURL;
    @Value("${whatsapp.api.token}")
    private String bearerToken;
    @Value("${whatsapp.phone.number.test}")
    private String phoneNumber;

    public String sendMessageToWhatsApp(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);
        RequestMessage messageRequest = formatMessage(message);
        HttpEntity<RequestMessage> entity = new HttpEntity<>(messageRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(whatsappWebhookURL, entity, String.class);
        return responseEntity.getBody();
    }

    public String sendMessageProcessed(String messageJson) {
        Gson gson = new Gson();
        ResponseMessage responseMessage = gson.fromJson(messageJson, ResponseMessage.class);
        List<Message> listMessages = getMessages(responseMessage);
        if (listMessages != null && !listMessages.isEmpty()) {
            String phoneNumber = getPhoneNumberFromMessage(listMessages);
            String messageBody = getMessage(listMessages);
            if (!existPhoneNumber(phoneNumber)) {
                String name = getNameFromResponse(responseMessage);
                userRepository.save(new User(phoneNumber, name));
            }

            List<String> listResponse = dialogFlowService.getResponseMessageProcessed(messageBody);
            for (String response: listResponse) {
                sendMessageToWhatsApp(response);
            }

        }
        return "";
    }

    private List<Message> getMessages(ResponseMessage responseMessage){
        return responseMessage.getEntry().get(0).getChanges().get(0).getValue().getMessages();
    }

    private String getNameFromResponse(ResponseMessage responseMessage){
        return responseMessage.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName();
    }

    private String getMessage(List<Message> messages) {
        return messages.get(0).getText().getBody();
    }

    private String getPhoneNumberFromMessage(List<Message> messages) {
        return messages.get(0).getFrom();
    }

    private boolean existPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
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
