package com.bootcamp.service.transaction.service.impl;

import com.bootcamp.service.transaction.constants.MovementTypeConstants;
import com.bootcamp.service.transaction.mapper.TransactionMapper;
import com.bootcamp.service.transaction.model.Transaction;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import com.bootcamp.service.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    TransactionServiceImpl transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Test
    void createTransaction_whenTransactionRQIsNotNull() {

        //Arrage
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAmount(100.00);
        transactionRQ.setMovementType(MovementTypeConstants.DEPOSIT);
        transactionRQ.setProductId("11111111");
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
        transactionRS.setId("11111111");
        transactionRS.setProductType("SA");

        Mockito.when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        //Act
        Mono<TransactionRS> result = transactionService.createTransaction(Mono.just(transactionRQ));

        //Assert
        StepVerifier.create(result)
                .expectNext(transactionRS)
                .verifyComplete();

    }
}