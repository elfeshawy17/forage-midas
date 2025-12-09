package com.jpmc.midascore.service.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jpmc.midascore.foundation.Transaction;

public interface TransactionService {
    void processTransaction(Transaction transactionDto);
}
