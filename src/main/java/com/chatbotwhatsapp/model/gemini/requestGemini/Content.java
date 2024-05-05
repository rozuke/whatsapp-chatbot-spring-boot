package com.chatbotwhatsapp.model.gemini.requestGemini;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    private List<Part> parts;
}
