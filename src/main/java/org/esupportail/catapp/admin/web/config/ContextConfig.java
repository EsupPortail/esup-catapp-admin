package org.esupportail.catapp.admin.web.config;

import org.esupportail.catapp.admin.web.utils.jsf.ViewScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Configuration
@Import({ ControllerConfig.class })
public class ContextConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer(@Value("#{systemProperties['config.location']?:''}") String configLocation) {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();

        Resource[] resources = new Resource[] {
                new ClassPathResource("/properties/defaults.properties")
        };

        if (!configLocation.isEmpty()) {
            final List<Resource> resourceList = new ArrayList<>(asList(resources));
            resourceList.add(new PathResource(configLocation));
            resources = resourceList.toArray(new Resource[resourceList.size()]);
        }

        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(true);

        return pspc;
    }



    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        Map<String, Object> scopes = new HashMap<String, Object>() {{
            put("view", new ViewScope());
        }};
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.setScopes(scopes);
        return configurer;
    }
}
