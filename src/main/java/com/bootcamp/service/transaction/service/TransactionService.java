package com.bootcamp.service.transaction.service;

import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionRS> createTransaction(Mono<TransactionRQ> transactionRQ);
}
