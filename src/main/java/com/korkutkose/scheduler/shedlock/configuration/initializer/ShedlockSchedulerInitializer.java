package com.korkutkose.scheduler.shedlock.configuration.initializer;

import com.korkutkose.scheduler.properties.ShedlockProperties;
import com.korkutkose.scheduler.shedlock.configuration.factory.RunnableTaskFactory;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * @author mehmetkorkut
 * created 10.11.2023 09:51
 * package - com.korkutkose.scheduler.shedlock.configuration
 * project - quartz-shedlock-migration
 */
@Component
@Slf4j
public class ShedlockSchedulerInitializer {

    private final ShedlockProperties shedlockProperties;
    private final LockProvider lockProvider;
    private final RunnableTaskFactory runnableTaskFactory;

    public ShedlockSchedulerInitializer(ShedlockProperties shedlockProperties,
                                        LockProvider lockProvider,
                                        RunnableTaskFactory runnableTaskFactory) {
        this.shedlockProperties = shedlockProperties;
        this.lockProvider = lockProvider;
        this.runnableTaskFactory = runnableTaskFactory;
    }

    /**
     * <p>initialize schedulers after application is ready</p>
     * <p>configure tasks with given parameters</p>
     * <p>if shedlockProperties.enabled() is false, then do not start schedulers</p>
     * <p>else start schedulers with given parameters</p>
     */
    @EventListener(ApplicationReadyEvent.class)
    public void configureTasks() {
        if (!shedlockProperties.enabled()) {
            log.warn("shedlockProperties.enabled() false. Not starting schedulers.");
            return;
        }
        List<BaseLockedRunnableJob> baseLockedRunnableJobs = getJobs();
        ThreadPoolTaskScheduler threadPoolTaskScheduler = taskScheduler(baseLockedRunnableJobs.size());
        baseLockedRunnableJobs.forEach(baseLockedRunnableJob -> generate(threadPoolTaskScheduler, baseLockedRunnableJob));
        log.info("# of schedulers generated: {}", baseLockedRunnableJobs.size());
    }

    /**
     * <p>get jobs with given parameters</p>
     *
     * @return list of BaseLockedRunnableJob instances with given parameters and lock provider from constructor parameter lockProvider instance
     */
    private List<BaseLockedRunnableJob> getJobs() {
        BaseLockedRunnableJob firstJob = getFirstJob(shedlockProperties.schedulers().firstScheduler());
        BaseLockedRunnableJob secondJob = getSecondJob(shedlockProperties.schedulers().secondScheduler());
        return List.of(firstJob, secondJob);
    }

    /**
     * <p>register first job with given parameters</p>
     *
     * @param schedulerDetail scheduler detail object which contains cron expression and lock durations
     * @return BaseLockedRunnableJob instance with given parameters and lock provider from constructor parameter lockProvider instance
     */
    private BaseLockedRunnableJob getFirstJob(ShedlockProperties.SchedulerProperties.SchedulerDetail schedulerDetail) {
        return getLockableJob(runnableTaskFactory.createDummyWaitingScheduledJob(schedulerDetail.key()),
                schedulerDetail.key(), schedulerDetail.lockAtMostFor(),
                schedulerDetail.lockAtLeastFor(), schedulerDetail.cron());
    }

    /**
     * <p>register second job with given parameters</p>
     *
     * @param schedulerDetail scheduler detail object which contains cron expression and lock durations
     * @return BaseLockedRunnableJob instance with given parameters and lock provider from constructor parameter lockProvider instance
     */
    private BaseLockedRunnableJob getSecondJob(ShedlockProperties.SchedulerProperties.SchedulerDetail schedulerDetail) {
        return getLockableJob(runnableTaskFactory.createAnotherDummyWaitingScheduledJob(schedulerDetail.key()),
                schedulerDetail.key(), schedulerDetail.lockAtMostFor(),
                schedulerDetail.lockAtLeastFor(), schedulerDetail.cron()
        );
    }

    /**
     * <p>generate job with given parameters</p>
     *
     * @param threadPoolTaskScheduler thread pool task scheduler instance to be used for scheduling
     * @param lockableJob             lockable job instance which contains runnable task and lock provider
     */
    private void generate(ThreadPoolTaskScheduler threadPoolTaskScheduler,
                          BaseLockedRunnableJob lockableJob) {
        threadPoolTaskScheduler.schedule(lockableJob, new CronTrigger(lockableJob.getCron()));
        log.info("{} is scheduled to run with cron {} and durations; lockAtLeastFor: {}, lockAtMostFor: {}",
                lockableJob.getLockName(), lockableJob.getCron(), lockableJob.getLockAtLeastFor(), lockableJob.getLockAtMostFor());
    }

    /**
     * <p>create lockable job with given parameters</p>
     *
     * @param task           task to be executed
     * @param lockName       name of the lock
     * @param lockAtMostFor  <p>specifies the maximum duration for which the lock should be held. It limits the maximum execution time of the task.
     * @param lockAtLeastFor <p>specifies the minimum duration for which the lock should be held.
     * @param cron           <p>cron expression to be used for scheduling
     * @return BaseLockedRunnableJob instance with given parameters and lock provider from constructor parameter lockProvider instance
     */
    private BaseLockedRunnableJob getLockableJob(Runnable task, String lockName, Duration lockAtMostFor, Duration lockAtLeastFor, String cron) {
        return new BaseLockedRunnableJob(task, lockName, lockProvider, lockAtMostFor, lockAtLeastFor, cron);
    }

    /**
     * let's use separate thread pool for shedlock
     *
     * @return ThreadPoolTaskScheduler instance with given properties from application.yml file under shedlock prefix properties (thread)
     */
    private ThreadPoolTaskScheduler taskScheduler(int size) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        ShedlockProperties.ThreadDefinition thread = shedlockProperties.thread();
        scheduler.setPoolSize(thread.useFixedPoolSize() ? thread.corePoolSize() : size);
        scheduler.setThreadNamePrefix(thread.prefix());
        scheduler.initialize();
        return scheduler;
    }
}
