package com.Springboot.mybankapp2.service;

import com.Springboot.mybankapp2.Entity.Transaction;
import com.Springboot.mybankapp2.dto.Response;
import com.Springboot.mybankapp2.dto.TransactionDTO;
import com.Springboot.mybankapp2.dto.TransactionHistory;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
    List<TransactionDTO> getAllTransaction();

    Transaction fetchByAccountNumber(TransactionHistory transactionHistory);
    List <TransactionDTO> getByAccountNumber(TransactionHistory transactionHistory);
}
