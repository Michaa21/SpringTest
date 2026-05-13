package com.example.springtest.config;

import feign.FeignException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Set;
import java.util.function.Predicate;

public class ExternalServiceRetryPredicate implements Predicate<Throwable> {

    private static final Set<Integer> RETRYABLE_STATUSES = Set.of(
            500, 502, 503, 504);

    @Override
    public boolean test(Throwable throwable) {
        Integer status = extreactStatus(throwable);

        return status != null && RETRYABLE_STATUSES.contains(status);
    }

    private Integer extreactStatus(Throwable throwable) {
        if (throwable instanceof FeignException feignException) {
            return feignException.status();
        }

        if (throwable instanceof HttpStatusCodeException httpStatusCodeException) {
            return httpStatusCodeException.getStatusCode().value();
        }

        Throwable cause = throwable.getCause();

        if (cause != null && cause != throwable) {
            return extreactStatus(cause);
        }
        return null;
    }
}

