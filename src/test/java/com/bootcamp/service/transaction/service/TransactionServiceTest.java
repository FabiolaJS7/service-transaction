package com.bootcamp.service.transaction.service;

import com.bootcamp.service.transaction.constants.MovementTypeConstants;
import com.bootcamp.service.transaction.model.Transaction;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import com.bootcamp.service.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @Disabled
    @Test
    void createTransaction_whenTransactionRQIsNotNull() {
        //Arrage
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAmount(100.00);
        transactionRQ.setMovementType(MovementTypeConstants.DEPOSIT);
        transactionRQ.setProductId("ID-001");
        transactionRQ.setProductType("SA");

        Transaction transaction = new Transaction();
        transaction.setId("ID-001");
        transaction.setAmount(100.00);
        transaction.setMovementType(MovementTypeConstants.DEPOSIT);
        transaction.setProductId("11111111");
        transaction.setProductType("SA");

        TransactionRS transactionRS = new TransactionRS();
        transactionRS.setAmount(100.00);
        transactionRS.setMovementType(MovementTypeConstants.DEPOSIT);
        transactionRS.setProductType("SA");
        transactionRS.setProductId("ID-001");

        Mono<TransactionRS> transactionRSMono = transactionService.createTransaction(Mono.just(transactionRQ));

        StepVerifier.create(transactionRSMono.map(TransactionRS::getProductId))
                .expectNext(transactionRS.getProductId())
                .verifyComplete();

    }
}