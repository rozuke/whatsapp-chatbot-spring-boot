package com.chatbotwhatsapp.controller;

import com.chatbotwhatsapp.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class WhatsAppController {


    @Autowired
    private WhatsAppService whatsAppService;

    @GetMapping("/webhook")
    public ResponseEntity<String> sendMessage(@RequestParam("hub.mode") String mode,
                                              @RequestParam("hub.challenge") String challenge,
                                              @RequestParam("hub.verify_token") String token) {

        return whatsAppService.webhookVerify(mode, challenge, token);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> postMessage(@RequestBody String json){
        return new ResponseEntity<>(whatsAppService.responseMessage(json), HttpStatus.OK);
    }

}
