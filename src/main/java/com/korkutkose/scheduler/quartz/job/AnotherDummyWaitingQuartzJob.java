package com.korkutkose.scheduler.quartz.job;

import com.korkutkose.business.service.TaskEntityDto;
import com.korkutkose.business.service.TaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.LocalDateTime;

import static com.korkutkose.scheduler.utils.Constants.MESSAGE_TOPIC_KEY;

/**
 * @author mehmetkorkut
 * @created 9.11.2023 14:25
 * @package - com.korkutkose.scheduler.quartz.job
 * @project - quartz-shedlock-migration
 */
@Slf4j
@DisallowConcurrentExecution
public class AnotherDummyWaitingQuartzJob implements Job {
    private final TaskService taskService;

    public AnotherDummyWaitingQuartzJob(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * fires that is associated with the <code>Job</code>.
     * </p>
     *
     * <p>
     * The implementation may wish to set a
     * {@link JobExecutionContext#setResult(Object) result} object on the
     * {@link JobExecutionContext} before this method exits.  The result itself
     * is meaningless to Quartz, but may be informative to
     * <code>{@link JobListener}s</code> or
     * <code>{@link TriggerListener}s</code> that are watching the job's
     * execution.
     * </p>
     *
     * @param context
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String topic = context.getJobDetail().getJobDataMap().getString(MESSAGE_TOPIC_KEY);
        log.info("AnotherDummyWaitingQuartzJob is triggered for topic: {}", topic);

        TaskEntityDto task = taskService.createTask(topic, topic + "-quartz-" + LocalDateTime.now());

        log.info("AnotherDummyWaitingQuartzJob is finished for topic: {} and created entity: {}", topic, task);
    }
}
