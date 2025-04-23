package com.bootcamp.service.transaction.service.impl;

import com.bootcamp.service.transaction.mapper.TransactionMapper;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import com.bootcamp.service.transaction.repository.TransactionRepository;
import com.bootcamp.service.transaction.service.CacheService;
import com.bootcamp.service.transaction.service.CorrelativeService;
import com.bootcamp.service.transaction.service.TransactionService;
import com.bootcamp.service.transaction.util.AuditDataUtil;
import com.bootcamp.service.transaction.util.DateUtil;
import com.bootcamp.service.transaction.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CorrelativeService correlativeService;
    @Autowired
    CacheService cacheService;

    @Override
    public Mono<TransactionRS> createTransaction(Mono<TransactionRQ> transactionRQ) {
        return transactionRQ.map(TransactionMapper.INSTANCE::toTransactionOfTransactionRQ)
                .doOnNext(t -> log.info("Getting transactionRQ to save {}", JsonTransferUtil.objectToJson(t)))
                .flatMap(transaction ->
                        correlativeService.getCorrelativeTransactionNumber()
                            .map(integer -> {
                                transaction.setTransactionNumber(integer);
                                transaction.setAuditData(AuditDataUtil.create());
                                return transaction;
                            })
                )
                .flatMap(transaction -> transactionRepository.save(transaction))
                .doOnSuccess(transaction -> log.info("Transaction saved"))
                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                .onErrorResume(Mono::error);
    }

    @Override
    public Flux<TransactionRS> getTransactions() {
        String cacheKey = "allTransactionss";

        // Intentar obtener las transacciones desde el caché
        return cacheService.get(cacheKey)
                .flatMapMany(cachedTransactions -> {
                    // Si las transacciones están en el caché, devolverlas
                    log.info("Transactions retrieved from cache {}", JsonTransferUtil.objectToJson(cachedTransactions));
                    return Flux.fromIterable((List<TransactionRS>) cachedTransactions);
                })
                .switchIfEmpty(
                        // Si no están en el caché, obtenerlas de la base de datos
                        transactionRepository.findAll()
                                .doOnSubscribe(subscription -> log.info("Start getting transactions from database"))
                                .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                                .collectList() // Convertir a lista para almacenarla en el caché
                                .flatMapMany(transactions -> {
                                    // Guardar las transacciones en el caché
                                    return cacheService.save(cacheKey, transactions)
                                            .thenMany(Flux.fromIterable(transactions));
                                })
                )
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
    public Flux<TransactionRS> getTransactionsByProductIdAndDates(String productId, LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null) {
            log.info("Getting transactions by productid {}, startDate {}, endDate {}", productId, startDate, endDate);
            return transactionRepository
                    .findTransactionsByProductIdAndAuditDataCreatedAtBetween(productId,
                            Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .doOnSubscribe(subscription -> log.info("Getting transactions by product {}, between {} and {}", productId, startDate, endDate))
                    .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                    .doOnComplete(() -> log.info("End getting transactions by product {}, between {}", productId, startDate))
                    .doOnError(throwable -> log.error("Error getting transactions by product {}", productId, throwable));
        } else {
            return transactionRepository.findTransactionsByProductId(productId)
                    .doOnSubscribe(subscription -> log.info("Getting transactions by product id {}", productId))
                    .map(TransactionMapper.INSTANCE::toTransactionRSOfTransaction)
                    .doOnComplete(() -> log.info("End getting transactions by productId"))
                    .doOnError(throwable -> log.error("Error getting transactions by product id", throwable));
        }

    }
}
