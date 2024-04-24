package com.chatbotwhatsapp.model.gemini;

import com.chatbotwhatsapp.model.gemini.requestGemini.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestGemini {

    private List<Content> contents;

}

