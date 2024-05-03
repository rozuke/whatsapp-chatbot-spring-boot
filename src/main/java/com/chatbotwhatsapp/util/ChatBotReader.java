package com.chatbotwhatsapp.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.Reader;

public class ChatBotReader {

    public static String getPrompt() {
        String jsonFilePath = "bot.json";

        try (Reader reader = new FileReader(jsonFilePath)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            JsonObject promptObject = jsonObject.getAsJsonObject("prompt");
            return promptObject.get("text").getAsString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
