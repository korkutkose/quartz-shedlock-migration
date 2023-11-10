package com.korkutkose;

import com.korkutkose.business.properties.ApplicationProperties;
import com.korkutkose.scheduler.properties.QuartzProperties;
import com.korkutkose.scheduler.properties.ShedlockProperties;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({QuartzProperties.class, ShedlockProperties.class, ApplicationProperties.class})
@EnableSchedulerLock(defaultLockAtMostFor = "PT15M")
public class QuartzShedlockMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzShedlockMigrationApplication.class, args);
    }

}
