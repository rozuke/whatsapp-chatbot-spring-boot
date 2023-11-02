package com.chatbotwhatsapp.model.whatsapp.responseMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseMessage {
    private String object;
    private List<Entry> entry;
}

