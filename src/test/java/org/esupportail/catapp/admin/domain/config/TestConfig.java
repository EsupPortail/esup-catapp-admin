package org.esupportail.catapp.admin.domain.config;

import org.esupportail.catapp.admin.domain.services.IApplicationService;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.domain.services.mocks.MockApplicationService;
import org.esupportail.catapp.admin.domain.services.mocks.MockDomaineService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class TestConfig {

    @Bean
    public IDomaineService mockDomaineService() {
        return new MockDomaineService();
    }

    @Bean
    public IApplicationService mockApplicationService() {
        return new MockApplicationService();
    }
}
