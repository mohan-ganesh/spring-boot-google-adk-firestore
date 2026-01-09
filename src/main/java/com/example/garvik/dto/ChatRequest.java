package com.example.garvik.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author Mohan Ganesh
 * @version 1.0
 * @since 2026-01-09
 * 
 *        Chat request
 */
public class ChatRequest {

    @Schema(description = "The message to send to the agent", example = "What's the time in New York?")
    private String message;

    @Schema(description = "The session ID for the chat context", example = "session-123")
    private String sessionId;

    @Schema(description = "The user ID for tracking", example = "user-456")
    private String userId;

    public ChatRequest() {
    }

    /**
     * 
     * @param message
     * @param sessionId
     * @param userId
     */
    public ChatRequest(String message, String sessionId, String userId) {
        this.message = message;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    /**
     * 
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
