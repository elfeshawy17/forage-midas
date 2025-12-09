package com.jpmc.midascore.service.transaction.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.exception.InsufficientBalanceException;
import com.jpmc.midascore.exception.InvalidTransactionException;
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
        // Step 1: Convert incoming JSON message to a Transaction DTO
        Transaction transactionDto = objectMapper.readValue(message, Transaction.class);

        float amount = transactionDto.getAmount();
        // Step 2: Validate the transaction amount (must be positive)
        if (amount <= 0) {
            throw new InvalidTransactionException("Amount must be greater than zero.");
        }

        Long senderId = transactionDto.getSenderId();
        Long recipientId= transactionDto.getRecipientId();

        // Step 3: Ensure sender and recipient are not the same user
        if (senderId.equals(recipientId)) {
            throw new InvalidTransactionException("Sender cannot send money to himself.");
        }

        // Step 4: Lock the users in a consistent order to prevent deadlocks
        UserRecord sender;
        UserRecord recipient;

        if (senderId < recipientId) {
            sender = databaseConduit.lockUser(senderId);
            recipient = databaseConduit.lockUser(recipientId);
        } else {
            recipient = databaseConduit.lockUser(recipientId);
            sender = databaseConduit.lockUser(senderId);
        }

        // Step 5: Check if the sender has sufficient balance before performing the transfer
        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }

        // Step 6: Perform the balance transfer
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        // Step 7: Persist updated user balances to the database
        databaseConduit.saveUser(sender);
        databaseConduit.saveUser(recipient);

        // Step 8: Create and persist the transaction record
        TransactionRecord transactionRecord = new TransactionRecord(amount, sender, recipient);
        databaseConduit.saveTransaction(transactionRecord);
    }
}