package ru.ibs.gasu.gchp.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Класс для получения id пользователя из заголовка запроса OAM_REMOTE_USER. <br>
 * Пример использования:
 * <pre>
 * Long userId = UserContext.get().getUserId()
 * </pre>
 */
public class UserContext {

    private static Logger LOGGER = LoggerFactory.getLogger(UserContext.class);
    /**
     * Имя http-заголовка, хранящего ID пользователя при вызове SOAP методов
     */
    public static final String USERID_HTTP_HEADER_NAME = "OAM_REMOTE_USER";

    private Long userId;

    private UserContext(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public static UserContext get() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            Object objContext = requestAttributes.getAttribute(USERID_HTTP_HEADER_NAME, RequestAttributes.SCOPE_REQUEST);
            if (objContext == null || !(objContext instanceof UserContext)) {
                final UserContext userContext = new UserContext(getUserIdFromHeader());
                LOGGER.debug("UserId = " + userContext.getUserId());
                requestAttributes.setAttribute(USERID_HTTP_HEADER_NAME, userContext, RequestAttributes.SCOPE_REQUEST);
            }
            return (UserContext) requestAttributes.getAttribute(USERID_HTTP_HEADER_NAME, RequestAttributes.SCOPE_REQUEST);
        } else {
            LOGGER.error("Invalid Request Context");
        }
        return new UserContext(null);
    }

    private static Long getUserIdFromHeader() {
        if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (request != null) {
                String strValue = request.getHeader(USERID_HTTP_HEADER_NAME);
                if (strValue != null) {
                    try {
                        return Long.valueOf(strValue);
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Invalid " + USERID_HTTP_HEADER_NAME + " value: " + strValue);
                    }
                }
            }
        } else {
            LOGGER.error("Invalid Request Context");
        }
        return null;
    }
}
