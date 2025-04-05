package com.bootcamp.service.transaction.mapper;

import com.bootcamp.service.transaction.model.Transaction;
import com.bootcamp.service.transaction.model.TransactionRQ;
import com.bootcamp.service.transaction.model.TransactionRS;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    // Mapea TransactionRQ a Transaction
    Transaction toTransactionOfTransactionRQ(TransactionRQ transactionRQ);
    // Mapea Transaction a TransactionRS
    TransactionRS toTransactionRSOfTransaction(Transaction transaction);

}
