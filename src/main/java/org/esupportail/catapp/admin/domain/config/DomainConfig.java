package org.esupportail.catapp.admin.domain.config;

import org.esupportail.catapp.admin.domain.services.ApplicationServiceImpl;
import org.esupportail.catapp.admin.domain.services.DomaineServiceImpl;
import org.esupportail.catapp.admin.domain.services.IApplicationService;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.domain.services.mocks.MockApplicationService;
import org.esupportail.catapp.admin.domain.services.mocks.MockDomaineService;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Lazy
@Configuration
@Import({ ExceptionConfig.class })
public class DomainConfig {

    @Value("${rest.service.url}")
    private String urlRestService;

    @Value("#{systemProperties['use.mocks']?:false}")
    private boolean useMocks;

    public static final String domainsPath = "domains";

    public static final String appsPath = "applications";

    @Bean
    public WebTarget restService() {
        final ClientConfig config = new ClientConfig();
        final Client client = ClientBuilder.newClient(config);
        return client.target(urlRestService);
    }

    @Bean
    public WebTarget restDomainesService() {
        return restService().path(domainsPath);
    }

    @Bean
    public WebTarget restApplicationsService() {
        return restService().path(appsPath);
    }

    @Bean
    public IApplicationService applicationService() {
        if (useMocks) {
            return new MockApplicationService();
        }
        return new ApplicationServiceImpl();
    }

    @Bean
    public IDomaineService domaineService() {
        if (useMocks) {
            return new MockDomaineService();
        }
        return new DomaineServiceImpl();
    }
}
