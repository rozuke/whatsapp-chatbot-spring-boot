package com.chatbotwhatsapp.model.whatsapp.responseMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Change {
    private Value value;
    private String field;
}
