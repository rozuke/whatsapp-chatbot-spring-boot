package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.whatsapp.responseMessage.ResponseMessage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private WhatsAppClientService whatsAppService;


    public ResponseEntity<String> webhookVerify(String mode, String challenge, String token) {
        System.out.println(mode);
        System.out.println(challenge);
        System.out.println(token);
        if (mode.equals("subscribe") && token.equals("Test")) {
            System.out.println("Te envio un mensaje desde el get");
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Verification token or mode mismatch", HttpStatus.FORBIDDEN);
        }
    }

    public String responseMessage(String messageJson) {
        Gson gson = new Gson();
        ResponseMessage requestMessage = gson.fromJson(messageJson, ResponseMessage.class);
        var listMessages = requestMessage.getEntry().get(0).getChanges().get(0).getValue().getMessages();

        if (listMessages != null && !listMessages.isEmpty()) {
            String messageBody = listMessages.get(0).getText().getBody();

            return whatsAppService.sendPostRequestMessage(messageBody);
        }

        return "";
    }

}
