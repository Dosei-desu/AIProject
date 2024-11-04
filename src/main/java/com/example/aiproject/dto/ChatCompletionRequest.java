package com.example.aiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatCompletionRequest {

    private String model;
    private List<Message> messages = new ArrayList<>();
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
