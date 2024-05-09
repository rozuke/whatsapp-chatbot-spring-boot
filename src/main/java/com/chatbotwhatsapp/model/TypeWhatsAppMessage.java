package com.chatbotwhatsapp.model;

import com.google.gson.JsonObject;

public record TypeWhatsAppMessage(String type, JsonObject content) {
}
