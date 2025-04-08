package com.bootcamp.service.transaction.service.impl;

import com.bootcamp.service.transaction.mapper.TransactionMapper;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import com.bootcamp.service.transaction.repository.TransactionRepository;
import com.bootcamp.service.transaction.service.TransactionService;
import com.bootcamp.service.transaction.util.AuditDataUtil;
import com.bootcamp.service.transaction.util.DateUtil;
import com.bootcamp.service.transaction.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<TransactionRS> createTransaction(Mono<TransactionRQ> transactionRQ) {
        return transactionRQ.map(TransactionMapper.INSTANCE::toTransactionOfTransactionRQ)
                .doOnNext(t -> log.info("Getting transactionRQ to save {}", JsonTransferUtil.objectToJson(t)))
                .flatMap(transaction -> {
                    //Agregando las fechas de creación para auditoría
                    transaction.setAuditData(AuditDataUtil.create());
                    return transactionRepository.save(transaction);
                })
                .doOnSuccess(transaction -> log.info("Transaction saved"))
                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                .onErrorResume(Mono::error);
    }

    @Override
    public Flux<TransactionRS> getTransactions() {
        return transactionRepository.findAll()
                .doOnSubscribe(subscription -> log.info("Start getting transactions"))
                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                .doOnComplete(() -> log.info("End getting transactions"))
                .doOnError(throwable -> log.error("Error getting transactions", throwable));

    }

    @Override
    public Flux<TransactionRS> getTransactionsByCustomerId(String customerId) {
        return transactionRepository.findByCustomerIdAndAuditDataCreatedAtBetween(customerId, DateUtil.getStartDate(),
                        DateUtil.getEndDate())
                .doOnSubscribe(subscription -> log.info("Getting transaction by customer Id {}",
                        customerId))
                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                .doOnComplete(() -> log.info("End getting transaction by customer Id {}", customerId))
                .doOnError(throwable -> log.error("Error getting transaction by customer Id", throwable));
    }

    @Override
    public Flux<TransactionRS> getTransactionsByProductId(String productId) {
        return transactionRepository.findTransactionsByProductId(productId)
                .doOnSubscribe(subscription -> log.info("Getting transactions by product id {}", productId))
                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                .doOnComplete(() -> log.info("End getting transactions by productId"))
                .doOnError(throwable -> log.error("Error getting transactions by product id", throwable));
    }
}
