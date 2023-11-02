package com.chatbotwhatsapp.model.chatgpt.responseChatGPT;

import com.chatbotwhatsapp.model.chatgpt.MessageGPT;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    private int index;
    private MessageGPT message;
    @JsonProperty("finish_reason")
    private String finishReason;
}
