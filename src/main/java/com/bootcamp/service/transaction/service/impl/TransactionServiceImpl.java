package com.bootcamp.service.transaction.service.impl;

import com.bootcamp.service.transaction.mapper.TransactionMapper;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import com.bootcamp.service.transaction.repository.TransactionRepository;
import com.bootcamp.service.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<TransactionRS> createTransaction(Mono<TransactionRQ> transactionRQ) {
        return transactionRQ.map(TransactionMapper.INSTANCE::toTransactionOfTransactionRQ)
                .doOnNext(subscription -> log.info("Getting transactionRQ to save"))
                .flatMap(transaction -> transactionRepository.save(transaction))
                .doOnSuccess(transaction -> log.info("Transaction saved"))
                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                .onErrorResume(Mono::error);
    }
}
