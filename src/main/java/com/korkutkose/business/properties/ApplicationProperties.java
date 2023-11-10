package com.korkutkose.business.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mehmetkorkut
 * created 9.11.2023 15:36
 * package - com.korkutkose.scheduler.properties
 * project - quartz-shedlock-migration
 */
@ConfigurationProperties(prefix = "app-generic")
public record ApplicationProperties(boolean simulateSlowOperation, int minInSeconds, int maxInSeconds) {
}
