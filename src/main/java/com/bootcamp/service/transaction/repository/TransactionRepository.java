package com.bootcamp.service.transaction.repository;

import com.bootcamp.service.transaction.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findTransactionByCustomerId(String customerId);
    Flux<Transaction> findByCustomerIdAndAuditDataCreatedAtBetween(String customerId, Date startDate, Date endDate);
    Flux<Transaction> findTransactionsByProductId(String productId);
    Mono<Transaction> findTopByOrderByTransactionNumberDesc();
    Flux<Transaction> findTransactionsByProductIdAndAuditDataCreatedAtBetween(String productId, Date startDate, Date endDate);
}
