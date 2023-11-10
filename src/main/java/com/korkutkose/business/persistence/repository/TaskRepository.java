package com.korkutkose.business.persistence.repository;

import com.korkutkose.business.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author mehmetkorkut
 * created 10.11.2023 08:52
 * package - com.korkutkose.persistence.repository
 * project - quartz-shedlock-migration
 */
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
}