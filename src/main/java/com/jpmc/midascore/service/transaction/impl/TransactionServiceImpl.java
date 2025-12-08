package com.jpmc.midascore.service.transaction.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.exception.InsufficientBalanceException;
import com.jpmc.midascore.exception.InvalidTransactionException;
import com.jpmc.midascore.exception.UserNotFoundException;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.transaction.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
class TransactionServiceImpl implements TransactionService {

    private final ObjectMapper objectMapper;
    private final DatabaseConduit databaseConduit;

    TransactionServiceImpl(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public void processTransaction(String message) throws JsonProcessingException {
        Transaction transactionDto = objectMapper.readValue(message, Transaction.class);

        if (transactionDto.getAmount() <= 0) {
            throw new InvalidTransactionException("Amount must be greater than zero.");
        }

        UserRecord sender = databaseConduit.findUserById(transactionDto.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("Sender not found."));

        UserRecord recipient = databaseConduit.findUserById(transactionDto.getRecipientId())
                .orElseThrow(() -> new UserNotFoundException("Recipient not found."));

        if (sender.getId().equals(recipient.getId())) {
            throw new InvalidTransactionException("Sender cannot send money to himself.");
        }

        float amount = transactionDto.getAmount();

        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        databaseConduit.saveUser(sender);
        databaseConduit.saveUser(recipient);

        TransactionRecord transactionRecord = new TransactionRecord(amount, sender, recipient);
        databaseConduit.saveTransaction(transactionRecord);
    }
}
