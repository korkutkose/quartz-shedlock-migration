package com.korkutkose.business.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author mehmetkorkut
 * created 10.11.2023 08:38
 * package - com.korkutkose.persistence.entity
 * project - quartz-shedlock-migration
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "TASK")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdDate;

}
