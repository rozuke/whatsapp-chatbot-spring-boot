package com.chatbotwhatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Autowired
    private ResponseMessageService messageService;

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

        return messageService.sendMessageProcessed(messageJson);
    }



}
