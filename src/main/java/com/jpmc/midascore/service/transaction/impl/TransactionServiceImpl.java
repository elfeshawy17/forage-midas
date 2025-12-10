package com.jpmc.midascore.service.transaction.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.exception.InsufficientBalanceException;
import com.jpmc.midascore.exception.InvalidTransactionException;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.incentive.IncentiveClient;
import com.jpmc.midascore.service.transaction.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final DatabaseConduit databaseConduit;
    private final IncentiveClient incentiveClientl;

    public TransactionServiceImpl(DatabaseConduit databaseConduit, IncentiveClient incentiveClientl) {
        this.databaseConduit = databaseConduit;
        this.incentiveClientl = incentiveClientl;
    }

    @Transactional
    public void processTransaction(Transaction transactionDto) {
        Long senderId = transactionDto.getSenderId();
        Long recipientId= transactionDto.getRecipientId();
        float amount = transactionDto.getAmount();

        // Step 1: Validate the transaction amount (must be positive)
        if (amount <= 0) {
            throw new InvalidTransactionException("Amount must be greater than zero.");
        }

        // Step 2: Ensure sender and recipient are not the same user
        if (senderId.equals(recipientId)) {
            throw new InvalidTransactionException("Sender cannot send money to himself.");
        }

        // Step 3: Lock the users in a consistent order to prevent deadlocks
        UserRecord sender;
        UserRecord recipient;

        if (senderId < recipientId) {
            sender = databaseConduit.lockUser(senderId);
            recipient = databaseConduit.lockUser(recipientId);
        } else {
            recipient = databaseConduit.lockUser(recipientId);
            sender = databaseConduit.lockUser(senderId);
        }

        // Step 4: Check if the sender has sufficient balance before performing the transfer
        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }

        // Step 5: Perform the balance transfer
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        // Step 6: Persist updated user balances to the database
        databaseConduit.saveUser(sender);
        databaseConduit.saveUser(recipient);

        // Step 7: Create and persist the transaction record
        TransactionRecord transactionRecord = new TransactionRecord(amount, sender, recipient);
        databaseConduit.saveTransaction(transactionRecord);
    }
}