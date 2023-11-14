package com.korkutkose;

import com.korkutkose.scheduler.quartz.job.AnotherDummyWaitingQuartzJob;
import com.korkutkose.scheduler.quartz.job.DummyWaitingQuartzJob;
import org.quartz.impl.jdbcjobstore.PostgreSQLDelegate;
import org.quartz.simpl.SimpleInstanceIdGenerator;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

import java.util.UUID;

/**
 * @author mehmetkorkut
 * created 10.11.2023 13:53
 * package - com.korkutkose
 * project - quartz-shedlock-migration
 */
public class NativeHintsRegistrar implements RuntimeHintsRegistrar {
    /**
     * Register hints for the given {@link RuntimeHints}.
     *
     * @param hints       the runtime hints to register
     * @param classLoader the class loader used to load the application
     */
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("db/ddl/*.sql");
        hints.resources().registerPattern("db/dml/*.sql");
        hints.resources().registerPattern("*.properties");

        //https://hibernate.atlassian.net/browse/HHH-16809
        //Class java.util.UUID[] is instantiated reflectively but was never registered.Register
        hints.reflection().registerType(UUID.class, MemberCategory.values());
        hints.reflection().registerType(UUID[].class, MemberCategory.values());

        //Unable to instantiate InstanceIdGenerator class: org.quartz.simpl.SimpleInstanceIdGenerator
        hints.reflection().registerType(SimpleInstanceIdGenerator.class, MemberCategory.values());
        //JobStore class 'org.springframework.scheduling.quartz.LocalDataSourceJobStore' props could not be configured.
        hints.reflection().registerType(LocalDataSourceJobStore.class, MemberCategory.values());
        //Couldn't load delegate class: org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
        hints.reflection().registerType(PostgreSQLDelegate.class, MemberCategory.values());

        //org.quartz.JobPersistenceException: Couldn't retrieve job because a required class was not found
        hints.reflection().registerType(AnotherDummyWaitingQuartzJob.class, MemberCategory.values());
        hints.reflection().registerType(DummyWaitingQuartzJob.class, MemberCategory.values());
    }
}
