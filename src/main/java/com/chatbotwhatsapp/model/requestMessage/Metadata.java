package com.chatbotwhatsapp.model.requestMessage;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Metadata {
    @SerializedName("display_phone_number")
    private String displayPhoneNumber;
    @SerializedName("phone_number_id")
    private String phoneNumberId;

}
