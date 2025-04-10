package com.bootcamp.service.transaction.service;

import reactor.core.publisher.Mono;

public interface CorrelativeService {
    Mono<Integer> getCorrelativeTransactionNumber();
}
