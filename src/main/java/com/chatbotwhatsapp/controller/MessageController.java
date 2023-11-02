package com.chatbotwhatsapp.controller;

import com.chatbotwhatsapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping()
public class MessageController {

    private final Lock messageLock = new ReentrantLock();
    private int count = 0;

    @Autowired
    private MessageService messageService;

    @GetMapping("/webhook")
    public ResponseEntity<String> sendMessage(@RequestParam("hub.mode") String mode,
                                              @RequestParam("hub.challenge") String challenge,
                                              @RequestParam("hub.verify_token") String token) {

        return messageService.webhookVerify(mode, challenge, token);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> postMessage(@RequestBody String json){
        return new ResponseEntity<>(messageService.responseMessage(json), HttpStatus.OK);
    }

}
