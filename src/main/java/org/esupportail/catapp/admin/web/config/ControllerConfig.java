package org.esupportail.catapp.admin.web.config;

import org.esupportail.catapp.admin.domain.config.DomainConfig;
import org.esupportail.catapp.admin.domain.services.IApplicationService;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.web.controllers.ApplicationController;
import org.esupportail.catapp.admin.web.controllers.DomaineController;
import org.springframework.context.annotation.*;

import javax.inject.Inject;

@Lazy
@Configuration
@Import({ DomainConfig.class})
public class ControllerConfig {

    @Inject
    private IApplicationService applicationService;

    @Inject
    private IDomaineService domaineService;

    @Bean
    @Scope("view")
    public ApplicationController applicationController() {
        return ApplicationController.applicationController(applicationService, domaineService);
    }

    @Bean
    @Scope("view")
    public DomaineController domaineController() {
        return DomaineController.domaineController(domaineService);
    }
}
