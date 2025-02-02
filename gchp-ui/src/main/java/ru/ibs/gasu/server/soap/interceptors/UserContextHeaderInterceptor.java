package ru.ibs.gasu.server.soap.interceptors;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import ru.ibs.gasu.server.user.UserContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserContextHeaderInterceptor extends AbstractPhaseInterceptor<Message> {

    public UserContextHeaderInterceptor() {
        super(Phase.POST_LOGICAL);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Map<String, List> headers = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        if (headers == null) {
            headers = new HashMap<>();
            message.put(Message.PROTOCOL_HEADERS, headers);
        }
        if (UserContext.get() != null && UserContext.get().getUserId() != null) {
            headers.put(UserContext.USERID_HTTP_HEADER_NAME, Collections.singletonList(Long.toString(UserContext.get().getUserId())));
        } else {
            // Добавляет, убирает возможность работать без id пользователя в заголовке запроса.
            // throw new Fault(new Throwable("Пользователь не авторизован!"));
        }
    }
}
