package com.bootcamp.service.transaction.service.impl;

import com.bootcamp.service.transaction.model.Transaction;
import com.bootcamp.service.transaction.repository.TransactionRepository;
import com.bootcamp.service.transaction.service.CorrelativeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class CorrelativeServiceImpl implements CorrelativeService {

    TransactionRepository transactionRepository;

    @Override
    public Mono<Integer> getCorrelativeTransactionNumber() {
        return transactionRepository.findTopByOrderByTransactionNumberDesc()
                .map(transaction -> transaction.getTransactionNumber() + 1)
                .doOnSuccess(transactionNumber -> log.info("Getting correlative transaction number {}", transactionNumber))
                .onErrorReturn(1)
                .doOnError(throwable -> log.error("Error getting correlative transaction number", throwable));
    }
}
