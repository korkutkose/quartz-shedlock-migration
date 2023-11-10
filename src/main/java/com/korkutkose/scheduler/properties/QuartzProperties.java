package com.korkutkose.scheduler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

/**
 * @author mehmetkorkut
 * created 9.11.2023 15:36
 * package - com.korkutkose.scheduler.properties
 * project - quartz-shedlock-migration
 */
@ConfigurationProperties(prefix = "quartz")
public record QuartzProperties(boolean enabled, String classpath,
                               int startupDelayInSecond, SchedulerProperties schedulers) {
    public record SchedulerProperties(SchedulerDetail firstScheduler, SchedulerDetail secondScheduler) {
        public record SchedulerDetail(String key, long repeatIntervalInMs) {

        }
    }
}
