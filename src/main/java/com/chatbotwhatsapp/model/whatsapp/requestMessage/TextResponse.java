package com.chatbotwhatsapp.model.whatsapp.requestMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextResponse {
    @JsonProperty("preview_url")
    private boolean previewUrl;
    private String body;
}
