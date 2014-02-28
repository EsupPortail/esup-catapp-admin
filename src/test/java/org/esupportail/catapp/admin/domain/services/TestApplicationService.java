package org.esupportail.catapp.admin.domain.services;


import fj.data.List;
import fj.data.Option;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;

import static fj.data.Option.fromNull;
import static java.lang.String.format;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class, loader=AnnotationConfigContextLoader.class)
public class TestApplicationService {

    @Inject
    private IApplicationService mockApplicationService;

    @Test
    public void getApplication() throws Exception {
        final String code = "SIFAC-T04";
        final Option<ApplicationDTO> app = fromNull(mockApplicationService.getOne(code));

        assertTrue(format("applicaton with code [%s] does not exists!", code), app.isSome());
    }

    @Test
    public void getApplications() throws Exception {
        final List<ApplicationDTO> apps = mockApplicationService.getList();

        assertFalse("applicatons list is empty", apps.isEmpty());
    }
}
