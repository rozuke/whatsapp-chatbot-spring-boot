package com.chatbotwhatsapp.controller;

import com.chatbotwhatsapp.service.ResponseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class WhatsAppController {


    @Autowired
    private ResponseMessageService responseMessageService;

    @GetMapping("/webhook")
    public ResponseEntity<String> sendMessage(@RequestParam("hub.mode") String mode,
                                              @RequestParam("hub.challenge") String challenge,
                                              @RequestParam("hub.verify_token") String token) {

        return responseMessageService.webhookVerify(mode, challenge, token);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> postMessage(@RequestBody String json){
        return new ResponseEntity<>(responseMessageService.responseMessage(json), HttpStatus.OK);
    }

}
