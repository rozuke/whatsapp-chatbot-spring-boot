package com.chatbotwhatsapp.model.whatsapp.responseMessage;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contact {
    private Profile profile;
    @SerializedName("wa_id")
    private String waId;
}
