package com.chatbotwhatsapp.controller;

import com.chatbotwhatsapp.service.DialogFlowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dialogflow")
public class DialogFlowController {

    @Autowired
    private DialogFlowClient dialogFlowClient;

    @PostMapping("/webhook")
    public ResponseEntity<String> postProcessedRequest(@RequestBody String json) {
        System.out.println(json );
        return new ResponseEntity<>(dialogFlowClient.sendResponseFromWebhook(json), HttpStatus.OK);

    }
}
