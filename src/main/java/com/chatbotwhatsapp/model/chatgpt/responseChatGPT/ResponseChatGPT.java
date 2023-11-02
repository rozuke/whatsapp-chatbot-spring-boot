package com.chatbotwhatsapp.model.chatgpt.responseChatGPT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseChatGPT {
    private String id;
    private String object;
    private int created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
}
