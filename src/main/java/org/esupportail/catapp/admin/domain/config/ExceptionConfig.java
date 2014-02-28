package org.esupportail.catapp.admin.domain.config;

import org.esupportail.catapp.admin.domain.exceptions.ExceptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ SmtpConfig.class })
public class ExceptionConfig {

    @Bean
    public ExceptionUtils exceptionUtils() {
        return ExceptionUtils.exceptionUtils;
    }
}
