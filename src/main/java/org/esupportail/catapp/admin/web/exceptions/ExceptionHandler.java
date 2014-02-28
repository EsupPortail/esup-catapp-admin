package org.esupportail.catapp.admin.web.exceptions;

import org.primefaces.extensions.component.ajaxerrorhandler.AjaxExceptionHandler;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

import static org.esupportail.catapp.admin.domain.exceptions.ExceptionUtils.exceptionUtils;


public class ExceptionHandler extends AjaxExceptionHandler {
	
	public ExceptionHandler(javax.faces.context.ExceptionHandler wrapped) {
		super(wrapped);
	}
	
	@Override
	public void handle() throws FacesException {
		Iterable<ExceptionQueuedEvent> exceptionQueuedEvents = getUnhandledExceptionQueuedEvents();
		if (exceptionQueuedEvents != null
				&& exceptionQueuedEvents.iterator() != null) {
			Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = exceptionQueuedEvents.iterator();

			if (unhandledExceptionQueuedEvents.hasNext()) {
                final ExceptionQueuedEventContext eqec = unhandledExceptionQueuedEvents.next().getContext();
                final Throwable exception = eqec.getException();
				exceptionUtils.send(exception);

                if (exception instanceof ViewExpiredException) {
                    FacesContext context = eqec.getContext();
                    NavigationHandler navHandler = context.getApplication().getNavigationHandler();

                    try {
                        navHandler.handleNavigation(context, null, "/stylesheets/welcome.xhtml?faces-redirect=true&expired=expired");
                    } finally {
                        unhandledExceptionQueuedEvents.remove();
                    }
                }
			}
		}
		
		getWrapped().handle();
	}


}
