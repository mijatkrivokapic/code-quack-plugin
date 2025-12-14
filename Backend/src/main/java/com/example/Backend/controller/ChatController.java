package com.example.backend.controller;

import com.example.backend.dto.CreateMessageDTO;
import com.example.backend.dto.MessageResponseDTO;
import com.example.backend.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final QuestionService questionService;

    public ChatController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping(value ="/messages", produces = "application/json")
    public ResponseEntity<MessageResponseDTO> startSession(@RequestBody CreateMessageDTO questionDTO) {
        String response = questionService.askQuestion(questionDTO);
        return ResponseEntity.ok(new MessageResponseDTO(response));
    }


}
