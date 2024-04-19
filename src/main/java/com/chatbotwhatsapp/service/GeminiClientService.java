package com.chatbotwhatsapp.service;

import com.chatbotwhatsapp.service.interfaces.AIModelService;
import org.springframework.stereotype.Service;

@Service
public class GeminiClientService implements AIModelService {


    @Override
    public String getResponseFromAIModel(String context) {
        return null;
    }
}
