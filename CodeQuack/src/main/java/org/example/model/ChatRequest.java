package org.example.model;

import java.util.Collections;
import java.util.List;

public class ChatRequest {
    private List<Object> history; // Koristimo Object jer je lista prazna, ne treba nam Message klasa jos
    private String codeContext;
    private String userProblem;

    public ChatRequest(String codeContext, String userProblem) {
        this.history = Collections.emptyList(); // Uvek saljemo praznu istoriju (Stateless)
        this.codeContext = codeContext;
        this.userProblem = userProblem;
    }
}
