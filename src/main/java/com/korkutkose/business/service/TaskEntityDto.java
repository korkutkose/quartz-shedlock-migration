package com.korkutkose.business.service;

import com.korkutkose.business.persistence.entity.TaskEntity;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link TaskEntity}
 */
public record TaskEntityDto(UUID id, String title, String description, boolean completed) implements Serializable {
}