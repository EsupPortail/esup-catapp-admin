package org.esupportail.catapp.admin.web.config.rewrite;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Log;
import org.ocpsoft.rewrite.servlet.config.*;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

import static org.ocpsoft.logging.Logger.Level.INFO;
import static org.ocpsoft.logging.Logger.Level.WARN;

public class Navigation extends HttpConfigurationProvider {

    @Override
    public int priority() { return 10; }

    @Override
    public Configuration getConfiguration(final ServletContext context) {
        return ConfigurationBuilder.begin()

                .addRule(Join.path("/").to("/stylesheets/welcome.xhtml").withInboundCorrection())
                .when(Direction.isInbound()
                        .and(Path.captureIn("path")))
                .perform(Log.message(INFO, "Requested Path => {path}"))

                .addRule()
                .when(Direction
                        .isInbound()
                        .and(DispatchType.isRequest())
                        .and(Path.captureIn("path"))
                        .andNot(Path.matches("{*}javax.faces.resource{*}"))
                        .andNot(Path.matches("{*}.{png|jpg|gif}")))
                .perform(Log.message(WARN, "Path {path} not found !")
                        .and(Forward.to("/stylesheets/notfound.xhtml")))

                //////////////////////////////// OUTBOUND /////////////////////////////////

                // Outbound access rules

                ;
    }
}
