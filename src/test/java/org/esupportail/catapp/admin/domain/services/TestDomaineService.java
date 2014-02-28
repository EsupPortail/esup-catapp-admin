package org.esupportail.catapp.admin.domain.services;


import fj.data.List;
import fj.data.Option;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;

import static fj.data.Option.fromNull;
import static java.lang.String.format;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class, loader=AnnotationConfigContextLoader.class)
public class TestDomaineService {

    @Inject
    private IDomaineService mockDomaineService;

    @Test
    public void getDomaine() throws Exception {
        final String code = "ROOT";
        final Option<DomaineDTO> domain = fromNull(mockDomaineService.getOne(code));

        assertTrue(format("domain with code [%s] does not exists!", code), domain.isSome());
    }

    @Test
    public void getDomaines() throws Exception {
        final List<DomaineDTO> domains = mockDomaineService.getList();

        assertFalse("domains list is empty", domains.isEmpty());
    }
}
