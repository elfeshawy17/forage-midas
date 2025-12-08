package com.jpmc.midascore.service.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TransactionService {
    void processTransaction(String message) throws JsonProcessingException;
}
