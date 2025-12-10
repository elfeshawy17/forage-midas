package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.exception.UserNotFoundException;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void saveUser(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public UserRecord findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserRecord lockUser(Long id) {
        return userRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public void saveTransaction(TransactionRecord transactionRecord) {
        transactionRepository.save(transactionRecord);
    }

}