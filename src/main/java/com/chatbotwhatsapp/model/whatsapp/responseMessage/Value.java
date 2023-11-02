package com.chatbotwhatsapp.model.whatsapp.responseMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Value {
    private String messaging_product;
    private Metadata metadata;
    private List<Contact> contacts;
    private List<Message> messages;
}
