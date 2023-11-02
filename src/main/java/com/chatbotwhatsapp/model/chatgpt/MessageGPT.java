package com.chatbotwhatsapp.model.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageGPT {
    private String role;
    private String content;
}
