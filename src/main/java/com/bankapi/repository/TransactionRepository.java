package com.bankapi.repository;

import com.bankapi.model.Account;
import com.bankapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.targetAccount = :account ORDER BY t.createdAt DESC")
    List<Transaction> findAllByAccount(@Param("account") Account account);
}
