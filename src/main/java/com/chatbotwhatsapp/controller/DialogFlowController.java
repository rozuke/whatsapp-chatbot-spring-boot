package com.chatbotwhatsapp.controller;

import com.chatbotwhatsapp.service.DialogFlowClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dialogflow")
public class DialogFlowController {

    @Autowired
    private DialogFlowClient dialogFlowClient;


    @PostMapping("/webhook")
    public ResponseEntity<String> postProcessedRequest(@RequestBody String json) {

        return new ResponseEntity<>(dialogFlowClient.sendResponseFromWebhook(json), HttpStatus.OK);

    }

}
