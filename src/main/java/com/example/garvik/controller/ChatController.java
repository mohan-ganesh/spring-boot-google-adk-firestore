package com.example.garvik.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.example.garvik.agent.WeatherAgent;
import io.reactivex.rxjava3.core.Maybe;
import com.example.garvik.dto.ChatRequest;
import com.google.adk.sessions.SessionNotFoundException;
import com.google.adk.runner.FirestoreDatabaseRunner;
import com.google.adk.sessions.GetSessionConfig;
import com.google.cloud.firestore.Firestore;
import com.google.genai.types.Content;
import com.google.genai.types.Part;

/**
 * 
 * @author Mohan Ganesh
 * @version 1.0
 * @since 2026-01-09
 * 
 *        Chat controller
 */
@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final FirestoreDatabaseRunner firestoreDatabaseRunner;

    private final String agentName = "weather-agent";

    @Autowired
    public ChatController(Firestore firestore, WeatherAgent weatherAgent) {
        this.firestoreDatabaseRunner = new FirestoreDatabaseRunner(
                weatherAgent.getRootAgent(),
                this.agentName,
                new ArrayList<>(),
                firestore);
    }

    /**
     * Health check endpoint
     * 
     * @return
     */
    @GetMapping("/healthcheck")
    public String home() {
        return "Welcome to Google ADK Firestore Application!";
    }

    /**
     * Chat endpoint
     * 
     * @param request
     * @return
     */
    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String chat(@RequestBody ChatRequest request) {
        if (request == null || request.getMessage() == null || request.getSessionId() == null
                || request.getUserId() == null) {
            logger.error("Invalid request");
            return "Invalid request";
        }

        String sessionId = request.getSessionId();
        String userId = request.getUserId();
        Content userMsg = Content.fromParts(Part.fromText(request.getMessage()));
        GetSessionConfig config = GetSessionConfig.builder().build();

        return this.firestoreDatabaseRunner.sessionService()
                .getSession(this.agentName, userId, sessionId, Optional.of(config))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof SessionNotFoundException) {
                        logger.info("Session not found, creating new session for sessionId: {}", sessionId);
                        return this.firestoreDatabaseRunner.sessionService()
                                .createSession(this.agentName, userId, new ConcurrentHashMap<>(), sessionId)
                                .toMaybe();
                    }
                    logger.error("Error getting session", throwable);
                    return Maybe.error(throwable);
                })
                .flatMapPublisher(session -> this.firestoreDatabaseRunner.runAsync(userId, sessionId, userMsg))
                .filter(event -> event.content().isPresent())
                .map(event -> {
                    return event.content().get().parts().stream()
                            .map(part -> {
                                if (part instanceof Part) {
                                    return ((Part) part).text().orElse("");
                                }
                                return part.toString();
                            })
                            .collect(Collectors.joining());
                })
                .filter(text -> !text.isEmpty())
                .collect(StringBuilder::new, StringBuilder::append)
                .map(StringBuilder::toString)
                .blockingGet();
    }
}