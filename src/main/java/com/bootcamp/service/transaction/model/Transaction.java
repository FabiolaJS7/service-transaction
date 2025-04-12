package com.bootcamp.service.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private String id;
    private int transactionNumber;
    private String productId;
    private String customerId;
    private String productType;
    private Double amount;
    private Double amountMoved;
    private String result;
    private String observation;
    private String movementType;
    private AuditData auditData;
    private double commissionAmount;
}
