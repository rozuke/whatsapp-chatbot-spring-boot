package com.chatbotwhatsapp.util;

public enum FileType {
    VIDEO("video"),
    AUDIO("audio"),
    IMAGE("image"),
    DOCUMENT("document"),
    TEXT("text");

    private final String text;

    FileType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
