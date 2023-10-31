package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.model.requestMessage.RequestMessage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private HttpClientWhatsAppService whatsAppService;


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
        RequestMessage requestMessage = gson.fromJson(messageJson, RequestMessage.class);
        var listMessages = requestMessage.getEntry().get(0).getChanges().get(0).getValue().getMessages();
        String messageBody = "";
        if (listMessages != null) {
            messageBody = listMessages.get(0).getText().getBody();
            whatsAppService.sendPostRequestMessage(messageBody);
        }


        return messageBody;
    }

    private String sendMessage(String message) {

        return "";
    }

}
