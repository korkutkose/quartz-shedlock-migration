package com.korkutkose.scheduler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author mehmetkorkut
 * created 9.11.2023 15:36
 * package - com.korkutkose.scheduler.properties
 * project - quartz-shedlock-migration
 */
@ConfigurationProperties(prefix = "shedlock")
public record ShedlockProperties(boolean enabled, SchedulerProperties schedulers, ThreadDefinition thread) {

    public record ThreadDefinition(int corePoolSize, String prefix) {
    }

    public record SchedulerProperties(SchedulerDetail firstScheduler,
                                      SchedulerDetail secondScheduler) {
        public record SchedulerDetail(String key, Duration lockAtMostFor, Duration lockAtLeastFor, String cron) {

        }
    }
}
