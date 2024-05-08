package com.chatbotwhatsapp.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.Reader;
import java.util.regex.Pattern;

public class ChatBotConstant {

    private static final String URL_REGEX = "^(http|https)://([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,6})(:[0-9]{1,5})?(/.*)?$";
    public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public static final String PROMPT = getPrompt();

    private static String getPrompt() {
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
