package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
