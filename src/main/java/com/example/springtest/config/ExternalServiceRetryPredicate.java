package com.example.springtest.config;

import feign.FeignException;
import feign.RetryableException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.function.Predicate;

public class ExternalServiceRetryPredicate implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof RetryableException) {
            return true;
        }

        if (throwable instanceof ConnectException
                || throwable instanceof SocketTimeoutException
                || throwable instanceof IOException) {
            return true;
        }

        if (throwable instanceof FeignException feignException) {
            int status = feignException.status();
            return status >= 500 && status < 600;
        }

        return false;
    }
}
