package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.whatsapp.responseMessage.Message;
import com.chatbotwhatsapp.model.whatsapp.responseMessage.ResponseMessage;
import com.chatbotwhatsapp.persistence.crud.UserRepository;
import com.chatbotwhatsapp.persistence.entity.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsAppService {

    @Autowired
    private ResponseMessageService whatsAppService;

    @Autowired
    private UserRepository userRepository;

    @Value("${whatsapp.verification.token}")
    private String verificationToken;


    public ResponseEntity<String> webhookVerify(String mode, String challenge, String token) {
        if (mode.equals("subscribe") && token.equals(verificationToken)) {
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Verification token or mode mismatch", HttpStatus.FORBIDDEN);
        }
    }

    public String responseMessage(String messageJson) {

        Gson gson = new Gson();
        ResponseMessage responseMessage = gson.fromJson(messageJson, ResponseMessage.class);
        var listMessages = getListOfMessages(responseMessage);

        if (listMessages != null && !listMessages.isEmpty()) {
            String name = getNameFromResponse(responseMessage);
            String phoneNumber = getPhoneNumberFromMessage(listMessages);
            String messageBody = getMessage(listMessages);

            if (existPhoneNumber(phoneNumber)) {
                return whatsAppService.sendPostRequestMessage(messageBody);
            } else {
                whatsAppService.sendPostRequestMessage(messageBody);
                userRepository.save(new User(phoneNumber, name));

            }

        }
        return "";
    }

    private List<Message> getListOfMessages(ResponseMessage responseMessage){
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

}
