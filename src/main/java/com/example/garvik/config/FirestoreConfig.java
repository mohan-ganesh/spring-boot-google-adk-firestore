package com.example.garvik.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

/**
 * 
 * @author Mohan Ganesh
 * @version 1.0
 * @since 2026-01-09
 * 
 *        Firestore configuration
 */
@Configuration
@EnableConfigurationProperties
public class FirestoreConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirestoreConfig.class);

    /**
     * Firestore bean
     * 
     * @return
     */
    @Bean
    public Firestore firestore() {
        logger.info("Initializing Firestore");
        Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
        logger.info("Firestore initialized");
        return firestore;
    }

}
