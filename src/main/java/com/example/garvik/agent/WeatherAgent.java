package com.example.garvik.agent;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;

/**
 * 
 * @author Mohan Ganesh
 * @version 1.0
 * @since 2026-01-09
 * 
 *        Weather agent
 */
@Component
public class WeatherAgent {

    private static final Logger logger = LoggerFactory.getLogger(WeatherAgent.class);

    private BaseAgent ROOT_AGENT;

    @PostConstruct
    public void init() {
        logger.info("Initializing WeatherAgent");
        this.ROOT_AGENT = initAgent();
    }

    /**
     * Initialize the root agent
     * 
     * @return
     */
    private BaseAgent initAgent() {
        logger.info("Initializing WeatherAgent");
        return LlmAgent.builder()
                .name("weather-agent")
                .description("Tells the current time in a specified city")
                .instruction("""
                        You are a helpful assistant that tells the current time in a city.
                        Use the getCurrentTime tool for this purpose.
                        """)
                .model("gemini-2.5-flash")
                .tools(FunctionTool.create(WeatherAgent.class, "getCurrentTime"))
                .build();
    }

    /**
     * Get the root agent
     * 
     * @return
     */
    public BaseAgent getRootAgent() {
        logger.info("Getting WeatherAgent");
        return this.ROOT_AGENT;
    }

    /**
     * Get the current time for a given city
     * 
     * @param city
     * @return
     */
    @Schema(description = "Get the current time for a given city")
    public static Map<String, String> getCurrentTime(
            @Schema(name = "city", description = "Name of the city to get the time for") String city) {
        return Map.of(
                "city", city,
                "forecast", "The time is " + new Date().toString());
    }

}
