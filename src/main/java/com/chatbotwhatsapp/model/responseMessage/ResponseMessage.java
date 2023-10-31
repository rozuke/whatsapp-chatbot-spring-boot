package com.chatbotwhatsapp.model.responseMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    @JsonProperty("messaging_product")
    private String messagingProduct;
    @JsonProperty("recipient_type")
    private String recipientType;
    private String to;
    private String type;
    private TextResponse text;

}