package com.korkutkose.scheduler.shedlock.configuration.initializer;

import lombok.Getter;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;

import java.time.Duration;
import java.time.Instant;

/**
 * <p>Base class for locked runnable jobs</p>
 * <p>It is used to execute runnable jobs with lock</p>
 * <p>It is used to prevent multiple instances of the same job to be executed at the same time</p>
 *
 * @author mehmetkorkut
 * @created 9.11.2023 15:03
 * @package - com.korkutkose.scheduler.shedlock.configuration.base
 * @project - quartz-shedlock-migration
 */
@Getter
public class BaseLockedRunnableJob implements Runnable {

    private final Runnable runnable;
    private final LockProvider lockProvider;
    private final String lockName;
    private final Duration lockAtMostFor;
    private final Duration lockAtLeastFor;
    private final String cron;

    /**
     * @param runnable       runnable object <br>
     * @param lockName       name of the lock <br>
     * @param lockProvider   lock provider as there are multiple in this project scope <br>
     * @param lockAtMostFor  <p>specifies the maximum duration for which the lock should be held. It limits the maximum execution time of the task.
     *                       If the task exceeds the lockAtMostFor duration, the lock will be released to allow other instances of the task to execute.</p> <br>
     * @param lockAtLeastFor <p>specifies the minimum duration for which the lock should be held.
     *                       It ensures that the task will be executed for at least the specified duration,
     *                       even if the task completes earlier. If the task finishes execution before the lockAtLeastFor duration elapses,
     *                       the lock will still be held until the minimum duration has passed.</p>
     * @param cron           <p>cron expression to be used for scheduling</p>
     */
    protected BaseLockedRunnableJob(Runnable runnable,
                                    String lockName,
                                    LockProvider lockProvider,
                                    Duration lockAtMostFor,
                                    Duration lockAtLeastFor, String cron) {
        this.runnable = runnable;
        this.lockName = lockName;
        this.lockProvider = lockProvider;
        this.lockAtMostFor = lockAtMostFor;
        this.lockAtLeastFor = lockAtLeastFor;
        this.cron = cron;
    }

    /**
     * <p>execute runnable task with lock</p>
     * When an object implementing interface {@code Runnable} is used
     */
    @Override
    public void run() {
        LockingTaskExecutor executor = new DefaultLockingTaskExecutor(lockProvider);
        executor.executeWithLock(runnable, new LockConfiguration(Instant.now(), lockName, lockAtMostFor, lockAtLeastFor));
    }
}
