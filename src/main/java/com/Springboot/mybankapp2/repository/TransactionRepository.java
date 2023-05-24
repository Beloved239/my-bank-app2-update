package com.Springboot.mybankapp2.repository;

import com.Springboot.mybankapp2.Entity.Transaction;
import com.Springboot.mybankapp2.dto.TransactionDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}
