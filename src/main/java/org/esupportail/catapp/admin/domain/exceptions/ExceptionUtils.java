package org.esupportail.catapp.admin.domain.exceptions;

import org.esupportail.commons.mail.SmtpService;
import org.esupportail.commons.mail.model.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

public enum ExceptionUtils {

    exceptionUtils;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
	private SmtpService smtpService;

    @Inject
	private InternetAddress emailException;

	public void send(final Throwable t) {
        try {
            smtpService.send(MessageTemplate.create(t, emailException));
        } catch (MessagingException e) {
            log.error("Problem sending exception email", e);
        }
    }

    /**
     * Unwrap the nested causes of given exception as long as until it is not an instance of the given type and then
     * return it. If the given exception is already not an instance of the given type, then it will directly be
     * returned. Or if the exception, unwrapped or not, does not have a nested cause anymore, then it will be returned.
     * This is particularly useful if you want to unwrap the real root cause out of a nested hierarchy of
     * {@link javax.servlet.ServletException} or {@link FacesException}.
     * @param exception The exception to be unwrapped.
     * @param type The type which needs to be unwrapped.
     * @return The unwrapped root cause.
     */
    public static <T extends Throwable> Throwable unwrap(Throwable exception, Class<T> type) {
        while (type.isInstance(exception) && exception.getCause() != null) {
            exception = exception.getCause();
        }

        return exception;
    }

    /**
     * Unwrap the nested causes of given exception as long as until it is not an instance of {@link FacesException}
     * (Mojarra) or {@link ELException} (MyFaces) and then return it. If the given exception is already not an instance
     * of the mentioned types, then it will directly be returned. Or if the exception, unwrapped or not, does not have
     * a nested cause anymore, then it will be returned.
     * @param exception The exception to be unwrapped from {@link FacesException} and {@link ELException}.
     * @return The unwrapped root cause.
     * @since 1.4
     */
    public static <T extends Throwable> Throwable unwrap(Throwable exception) {
        return unwrap(unwrap(exception, FacesException.class), ELException.class);
    }

    /**
     * Returns <code>true</code> if the given exception or one of its nested causes is an instance of the given type.
     * @param exception The exception to be checked.
     * @param type The type to be compared to.
     * @return <code>true</code> if the given exception or one of its nested causes is an instance of the given type.
     */
    public static <T extends Throwable> boolean is(Throwable exception, Class<T> type) {
        for (;exception != null; exception = exception.getCause()) {
            if (type.isInstance(exception)) {
                return true;
            }
        }

        return false;
    }
}
