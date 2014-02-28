package org.esupportail.catapp.admin.web.config.rewrite;

import org.ocpsoft.rewrite.config.*;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.el.El;
import org.ocpsoft.rewrite.servlet.config.*;
import org.ocpsoft.rewrite.servlet.config.bind.RequestBinding;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.ocpsoft.logging.Logger.Level.ERROR;
import static org.ocpsoft.logging.Logger.Level.WARN;

public class Navigation extends HttpConfigurationProvider {

    @Override
    public int priority() { return 10; }

    @Override
    public Configuration getConfiguration(final ServletContext context) {
        return ConfigurationBuilder.begin()

                .addRule(Join.path("/domaines")
                        .to("/stylesheets/domaines/domaines.xhtml").withInboundCorrection())

                .addRule(Join.path("/applications")
                        .to("/stylesheets/applications/applications.xhtml").withInboundCorrection())

                .addRule(Join.path("/{expired}")
                        .to("/stylesheets/welcome.xhtml").withInboundCorrection())
                .where("expired").bindsTo(RequestBinding.parameter("expired"))

                .addRule(Join.path("/")
                        .to("/stylesheets/welcome.xhtml").withInboundCorrection())

                .addRule()
                .when(DispatchType.isRequest()
                        .andNot(Path.matches("{*}javax.faces.resource{*}"))
                        .andNot(Path.matches("{*}.{png|jpg|gif}")))
                .perform(SendStatus.code(404))

                //////////////////////////////// OUTBOUND /////////////////////////////////
                ;
    }
}
