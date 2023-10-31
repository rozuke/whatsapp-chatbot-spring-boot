package com.chatbotwhatsapp.model.requestMessage;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Entry {
    private String id;
    private List<Change> changes;

}
