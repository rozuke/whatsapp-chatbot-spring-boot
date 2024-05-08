package com.chatbotwhatsapp.model;

import com.chatbotwhatsapp.util.ContentType;

public record DialogflowMessage(ContentType type, String content) {
}
