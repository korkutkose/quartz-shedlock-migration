package com.korkutkose.scheduler.shedlock.configuration;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.support.KeepAliveLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.concurrent.Executors;

/**
 * @author mehmetkorkut
 * @created 9.11.2023 15:03
 * @package - com.korkutkose.scheduler.shedlock.configuration
 * @project - quartz-shedlock-migration
 */
@Configuration
public class ShedlockConfiguration {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new KeepAliveLockProvider(getJdbcTemplateLockProvider(dataSource), Executors.newSingleThreadScheduledExecutor());
    }

    private JdbcTemplateLockProvider getJdbcTemplateLockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .withDbUpperCase(true)
                        .usingDbTime()// Works on Postgres, MySQL, MariaDb, MS SQL, Oracle, DB2, HSQL and H2
                        .build()
        );
    }

}
