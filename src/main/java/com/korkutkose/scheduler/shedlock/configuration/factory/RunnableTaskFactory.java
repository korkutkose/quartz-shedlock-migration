package com.korkutkose.scheduler.shedlock.configuration.factory;

import com.korkutkose.business.service.TaskService;
import com.korkutkose.scheduler.shedlock.job.AnotherDummyWaitingScheduledJob;
import com.korkutkose.scheduler.shedlock.job.DummyWaitingScheduledJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>factory class for creating runnable tasks</p>
 *
 * @author mehmetkorkut
 * @created 14.07.2023 10:38
 * @package - aero.tav.tams.file.importer.app.configuration.scheduler.factory
 * @project - tams-file-importer-app
 * @see com.korkutkose.scheduler.shedlock.job.AnotherDummyWaitingScheduledJob
 * @see com.korkutkose.scheduler.shedlock.job.DummyWaitingScheduledJob
 */
@Component
@Slf4j
public class RunnableTaskFactory {

    private final TaskService taskService;

    public RunnableTaskFactory(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * <p>create dummy waiting scheduled job</p>
     *
     * @return dummy waiting scheduled job instance with task service injected to it
     */
    public DummyWaitingScheduledJob createDummyWaitingScheduledJob(String key) {
        DummyWaitingScheduledJob dummyWaitingScheduledJob = new DummyWaitingScheduledJob(taskService, key);
        log.info("Generating task for dummyWaitingScheduledJob");
        return dummyWaitingScheduledJob;
    }

    /**
     * <p>create another dummy waiting scheduled job</p>
     *
     * @return dummy waiting scheduled job instance with task service injected to it
     */
    public AnotherDummyWaitingScheduledJob createAnotherDummyWaitingScheduledJob(String key) {
        AnotherDummyWaitingScheduledJob anotherDummyWaitingScheduledJob = new AnotherDummyWaitingScheduledJob(taskService, key);
        log.info("Generating task for anotherDummyWaitingScheduledJob");
        return anotherDummyWaitingScheduledJob;
    }

}
