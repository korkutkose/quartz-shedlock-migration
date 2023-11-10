package com.korkutkose.scheduler.utils;

import lombok.experimental.UtilityClass;

/**
 * @author mehmetkorkut
 * created 9.11.2023 15:59
 * package - com.korkutkose.scheduler.utils
 * project - quartz-shedlock-migration
 */
@UtilityClass
public class Constants {
    public static final String JOB_GROUP_NAME = "quartz-jobs";
    public static final String TRIGGER_GROUP_NAME = "quartz-triggers";
    public static final String MESSAGE_TOPIC_KEY = "message-topic";
}
