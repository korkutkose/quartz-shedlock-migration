package com.korkutkose.scheduler.quartz.configuration;

import com.korkutkose.scheduler.properties.QuartzProperties;
import com.korkutkose.scheduler.quartz.configuration.initializer.QuartzSchedulerInitializer;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;

/**
 * @author mehmetkorkut
 * @created 9.11.2023 15:03
 * @package - com.korkutkose.scheduler.quartz.configuration
 * @project - quartz-shedlock-migration
 */
@Configuration
@EnableAutoConfiguration(exclude = QuartzAutoConfiguration.class)
public class QuartzConfiguration {

    private final ApplicationContext applicationContext;
    private final QuartzProperties quartzProperties;
    private final QuartzProperties.SchedulerProperties schedulerProperties;

    public QuartzConfiguration(ApplicationContext applicationContext, QuartzProperties quartzProperties) {
        this.applicationContext = applicationContext;
        this.quartzProperties = quartzProperties;
        this.schedulerProperties = quartzProperties.schedulers();
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new AutowiringSpringBeanJobFactory(applicationContext.getAutowireCapableBeanFactory());
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception {
        SchedulerFactoryBean factory = getSchedulerFactoryBean(dataSource, jobFactory, quartzProperties.startupDelayInSecond());
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        return factory;
    }

    @Bean
    public QuartzSchedulerInitializer schedulerInitializer(Scheduler schedulerFactoryBean) {
        return new QuartzSchedulerInitializer(schedulerFactoryBean, schedulerProperties, quartzProperties.enabled());
    }

    private SchedulerFactoryBean getSchedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, int startupDelay) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setAutoStartup(quartzProperties.enabled());
        if (quartzProperties.classpath() != null) {
            factory.setConfigLocation(new ClassPathResource(quartzProperties.classpath()));
        }
        factory.setOverwriteExistingJobs(false);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setStartupDelay(startupDelay);
        factory.setWaitForJobsToCompleteOnShutdown(true);
        factory.afterPropertiesSet();
        return factory;
    }

    private static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

        private final AutowireCapableBeanFactory autowireCapableBeanFactory;

        public AutowiringSpringBeanJobFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
            this.autowireCapableBeanFactory = autowireCapableBeanFactory;
        }

        @Override
        @NonNull
        protected Object createJobInstance(@NonNull TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            this.autowireCapableBeanFactory.autowireBean(job);
            return job;
        }
    }
}