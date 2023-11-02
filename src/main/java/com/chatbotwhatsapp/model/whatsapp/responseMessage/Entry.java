package com.chatbotwhatsapp.model.whatsapp.responseMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Entry {
    private String id;
    private List<Change> changes;

}
