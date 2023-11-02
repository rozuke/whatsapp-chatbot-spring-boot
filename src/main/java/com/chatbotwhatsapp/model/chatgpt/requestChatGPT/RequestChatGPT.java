package com.chatbotwhatsapp.model.chatgpt.requestChatGPT;

import com.chatbotwhatsapp.model.chatgpt.MessageGPT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestChatGPT {
    private String model;
    private List<MessageGPT> messages;
    private double temperature;
}
