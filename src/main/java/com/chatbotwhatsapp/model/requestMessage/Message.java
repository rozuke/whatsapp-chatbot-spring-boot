package com.chatbotwhatsapp.model.requestMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
    private String from;
    private String id;
    private String timestamp;
    private TextRequest text;
    private String type;
}
