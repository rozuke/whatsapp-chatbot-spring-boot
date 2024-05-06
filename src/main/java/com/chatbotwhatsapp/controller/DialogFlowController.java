package com.chatbotwhatsapp.controller;

import com.chatbotwhatsapp.service.DialogFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dialogflow")
public class DialogFlowController {

    @Autowired
    private DialogFlowService dialogFlowService;


    @PostMapping("/response")
    public ResponseEntity<List<String>> postProcessRequestFromPostman(@RequestBody String json) {
        return new ResponseEntity<>(dialogFlowService.getResponseMessageProcessed(json), HttpStatus.OK);
    }

}
