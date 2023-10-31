package com.chatbotwhatsapp.model.requestMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestMessage {
    private String object;
    private List<Entry> entry;
}

