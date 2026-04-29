package com.example.springtest.config;

import feign.FeignException;

import java.util.function.Predicate;

public class ExternalServiceRetryPredicate implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof FeignException feignException) {
            return feignException.status() >= 500;
        }
        return false;
    }
}
