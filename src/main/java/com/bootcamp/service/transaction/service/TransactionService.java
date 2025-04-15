package com.bootcamp.service.transaction.service;

import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


public interface TransactionService {

    Mono<TransactionRS> createTransaction(Mono<TransactionRQ> transactionRQ);
    Flux<TransactionRS> getTransactions();
    Flux<TransactionRS> getTransactionsByCustomerId(String customerId);
    Flux<TransactionRS> getTransactionsByProductIdAndDates(String productId, LocalDate startDate, LocalDate endDate);
}
