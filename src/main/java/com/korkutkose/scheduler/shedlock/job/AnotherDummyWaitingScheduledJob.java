package com.korkutkose.scheduler.shedlock.job;

import com.korkutkose.business.service.TaskEntityDto;
import com.korkutkose.business.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;

import java.time.LocalDateTime;

/**
 * @author mehmetkorkut
 * @created 9.11.2023 15:53
 * @package - com.korkutkose.scheduler.shedlock.job
 * @project - quartz-shedlock-migration
 */
@Slf4j
public class AnotherDummyWaitingScheduledJob implements Runnable {

    private final TaskService taskService;
    private final String topic;

    public AnotherDummyWaitingScheduledJob(TaskService taskService, String topic) {
        this.taskService = taskService;
        this.topic = topic;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LockAssert.assertLocked();
        log.info("AnotherDummyWaitingScheduledJob is triggered for topic: {}", topic);
        TaskEntityDto task = taskService.createTask(topic, topic + "-shedlock-" + LocalDateTime.now());
        log.info("AnotherDummyWaitingScheduledJob is finished for topic: {} and created entity: {}", topic, task);
    }
}
