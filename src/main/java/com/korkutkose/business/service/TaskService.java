package com.korkutkose.business.service;

import com.korkutkose.business.persistence.entity.TaskEntity;
import com.korkutkose.business.persistence.repository.TaskRepository;
import com.korkutkose.business.properties.ApplicationProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author mehmetkorkut
 * created 10.11.2023 08:52
 * package - com.korkutkose.service
 * project - quartz-shedlock-migration
 */
@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationProperties applicationProperties;
    private final Random random;

    public TaskService(TaskRepository taskRepository, ApplicationProperties applicationProperties) {
        this.taskRepository = taskRepository;
        this.applicationProperties = applicationProperties;
        this.random = new Random();
    }

    public TaskEntityDto createTask(String title, String description) {
        TaskEntity save = taskRepository.save(TaskEntity.builder()
                .title(title)
                .description(description)
                .completed(true)
                .createdDate(LocalDateTime.now())//just for simplicity instead of auditAware entity
                .build());
        simulateSlowOperation();
        return new TaskEntityDto(save.getId(), save.getTitle(), save.getDescription(), save.isCompleted());
    }

    @SneakyThrows
    private void simulateSlowOperation() {
        if (applicationProperties.simulateSlowOperation()) {
            Integer randomNumberInRange = getRandomNumberInRange(applicationProperties.minInSeconds(), applicationProperties.maxInSeconds());
            log.info("Sleeping for {} seconds", randomNumberInRange);
            TimeUnit.SECONDS.sleep(randomNumberInRange);
        }
    }

    private Integer getRandomNumberInRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

}
