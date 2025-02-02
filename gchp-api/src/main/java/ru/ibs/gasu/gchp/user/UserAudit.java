package ru.ibs.gasu.gchp.user;

import org.springframework.data.domain.AuditorAware;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * Класс пользовательского аудита при изменении записей
 */
public class UserAudit implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        UserContext userContext = UserContext.get();

        if (ObjectUtils.isEmpty(userContext) || ObjectUtils.isEmpty(userContext.getUserId())) {
            throw new RuntimeException("Пользователь не авторизован!");
        }

        return Optional.ofNullable(userContext.getUserId());
    }

}
