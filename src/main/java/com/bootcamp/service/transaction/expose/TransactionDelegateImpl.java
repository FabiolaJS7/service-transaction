package com.bootcamp.service.transaction.expose;

import com.bootcamp.service.transaction.api.ApiApiDelegate;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import com.bootcamp.service.transaction.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionDelegateImpl implements ApiApiDelegate {

    TransactionService transactionService;

    @Override
    public Mono<ResponseEntity<TransactionRS>> createTransaction(Mono<TransactionRQ> transactionRQ,
                                                                  ServerWebExchange exchange) {
        log.info("-> Create Transaction");
        return transactionService.createTransaction(transactionRQ)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }


    @Override
    public Mono<ResponseEntity<Flux<TransactionRS>>> findAll(ServerWebExchange exchange) {
        log.info("-> Find All Transactions");
        return Mono.just(ResponseEntity.ok(transactionService.getTransactions()));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionRS>>> getTransactionByCustomerId(String customerId,
                                                                                 ServerWebExchange exchange) {
        log.info("-> Get Transaction By Customer Id {}", customerId);
        return Mono.just(ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId)));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionRS>>> getTransactionsByProductId(String productId,
                                                                                 ServerWebExchange exchange) {
        log.info("-> Getting transactions by product Id");
        return Mono.just(ResponseEntity.ok(transactionService.getTransactionsByProductId(productId)));

    }

}
