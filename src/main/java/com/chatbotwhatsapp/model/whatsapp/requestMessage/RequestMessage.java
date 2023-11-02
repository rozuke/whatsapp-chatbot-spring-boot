package com.chatbotwhatsapp.model.whatsapp.requestMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {
    @JsonProperty("messaging_product")
    private String messagingProduct;
    @JsonProperty("recipient_type")
    private String recipientType;
    private String to;
    private String type;
    private TextResponse text;

}