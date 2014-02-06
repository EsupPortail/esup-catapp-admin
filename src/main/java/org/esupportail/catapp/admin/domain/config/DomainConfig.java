package org.esupportail.catapp.admin.domain.config;

import org.esupportail.catapp.admin.domain.services.IApplicationService;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.domain.services.mocks.MockApplicationService;
import org.esupportail.catapp.admin.domain.services.mocks.MockDomaineService;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Lazy
@Configuration
public class DomainConfig {

    @Value("${base.rest.service.url}")
    private String urlRestService;

    @Bean
    public WebTarget restService() {
        final ClientConfig config = new ClientConfig()
                .connectorProvider(new ApacheConnectorProvider());
        Client client = ClientBuilder.newClient(config);
        return client.target(urlRestService);
    }

    @Bean
    public WebTarget restDomainesService() {
        return restService().path("domains");
    }

    @Bean
    public WebTarget restApplicationsService() {
        return restService().path("applications");
    }

    @Bean
    public IApplicationService applicationService() {
        return new MockApplicationService();
    }

    @Bean
    public IDomaineService domaineService() {
        return new MockDomaineService();
    }
}
