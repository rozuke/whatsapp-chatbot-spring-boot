package com.chatbotwhatsapp.model.requestMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Change {
    private Value value;
    private String field;
}
