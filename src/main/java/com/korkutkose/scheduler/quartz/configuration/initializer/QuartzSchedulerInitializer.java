package com.korkutkose.scheduler.quartz.configuration.initializer;

import com.korkutkose.scheduler.properties.QuartzProperties;
import com.korkutkose.scheduler.quartz.job.AnotherDummyWaitingQuartzJob;
import com.korkutkose.scheduler.quartz.job.DummyWaitingQuartzJob;
import com.korkutkose.scheduler.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author mehmetkorkut
 * @created 9.11.2023 15:03
 * @package - com.korkutkose.scheduler.quartz.configuration
 * @project - quartz-shedlock-migration
 */
@Slf4j
public class QuartzSchedulerInitializer implements InitializingBean {

    private final Scheduler scheduler;
    private final boolean enabled;
    private final QuartzProperties.SchedulerProperties schedulerProperties;

    public QuartzSchedulerInitializer(Scheduler scheduler, QuartzProperties.SchedulerProperties schedulerProperties, boolean enabled) {
        this.scheduler = scheduler;
        this.enabled = enabled;
        this.schedulerProperties = schedulerProperties;
    }

    @Override
    public void afterPropertiesSet() throws SchedulerException {
        if (!enabled) {
            log.warn("Quartz scheduler is disabled");
            return;
        }
        scheduleJob(schedulerProperties.firstScheduler());
        scheduleAnotherJob(schedulerProperties.secondScheduler());
    }

    private void scheduleJob(QuartzProperties.SchedulerProperties.SchedulerDetail schedulerDetail) throws SchedulerException {
        generateDummyWaitingJob(schedulerDetail.key(), schedulerDetail.repeatIntervalInMs(), DummyWaitingQuartzJob.class);
    }

    private void scheduleAnotherJob(QuartzProperties.SchedulerProperties.SchedulerDetail schedulerDetail) throws SchedulerException {
        generateDummyWaitingJob(schedulerDetail.key(), schedulerDetail.repeatIntervalInMs(), AnotherDummyWaitingQuartzJob.class);
    }

    private void generateDummyWaitingJob(String key, long l, Class<? extends Job> clazz) throws SchedulerException {
        JobKey jobKey = new JobKey(key, Constants.JOB_GROUP_NAME);
        TriggerKey triggerKey = new TriggerKey(key, Constants.TRIGGER_GROUP_NAME);
        if (!scheduler.checkExists(triggerKey)) {
            JobDetail jb = JobBuilder.newJob().ofType(clazz)
                    .withIdentity(jobKey)
                    .withDescription(key + " job description")
                    .usingJobData(Constants.MESSAGE_TOPIC_KEY, key)
                    .storeDurably(false).build();
            SimpleTrigger st = TriggerBuilder.newTrigger().forJob(jb)
                    .withIdentity(triggerKey)
                    .withDescription(key + " trigger description")
                    .withSchedule(SimpleScheduleBuilder
                            .simpleSchedule()
                            .withIntervalInMilliseconds(l)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(jb, st);
            log.info("Quartz job {} with the trigger {} is created", jobKey, triggerKey);
        } else {
            log.info("Quartz job {} with the trigger {} already exists", jobKey, triggerKey);
        }
    }
}
