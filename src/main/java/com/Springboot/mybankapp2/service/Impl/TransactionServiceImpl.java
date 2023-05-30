package com.Springboot.mybankapp2.service.Impl;
import com.Springboot.mybankapp2.Entity.Transaction;
import com.Springboot.mybankapp2.dto.Response;
import com.Springboot.mybankapp2.dto.TransactionDTO;
import com.Springboot.mybankapp2.dto.TransactionHistory;
import com.Springboot.mybankapp2.repository.TransactionRepository;
import com.Springboot.mybankapp2.service.TransactionService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl (TransactionRepository transactionRepository){
        this.transactionRepository=transactionRepository;
    }
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
       Transaction newTransaction = Transaction.builder()
               .transactionType(transactionDTO.getTransactionType())
               .accountNumber(transactionDTO.getAccountNumber())
               .amount(transactionDTO.getAmount())
               .build();
       transactionRepository.save(newTransaction);
    }
    @Override
    public List<TransactionDTO> getAllTransaction() {
        List <Transaction> transaction = transactionRepository.findAll();
        List<TransactionDTO> response = new ArrayList<>();
        for (Transaction transaction1: transaction){
            response.add(TransactionDTO.builder()
                            .transactionType(transaction1.getTransactionType())
                            .accountNumber(transaction1.getAccountNumber())
                            .amount(transaction1.getAmount())
                    .build());
        }
        return response;
    }
    @Override
    public Transaction fetchByAccountNumber(TransactionHistory transactionHistory) {
     List<Response> responses = new ArrayList<>();

        Transaction transaction = (Transaction) transactionRepository.findByAccountNumber(transactionHistory.getAccountNumber());
     TransactionDTO transactionDTO = new TransactionDTO();

      transactionDTO.setAccountNumber(transaction.getAccountNumber());
      transactionDTO.setTransactionType(transaction.getTransactionType());
      transactionDTO.setAmount(transaction.getAmount());

       return transaction;
    }

    @Override
    public List<TransactionDTO> getByAccountNumber(TransactionHistory transactionHistory) {
        List<Transaction> transactionList = transactionRepository.findByAccountNumber(transactionHistory
                .getAccountNumber());
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        for (Transaction transaction: transactionList){

            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionType(transaction.getTransactionType())
                    .accountNumber(transaction.getAccountNumber())
                    .amount(transaction.getAmount())
                    .build();
            transactionDTOList.add(transactionDTO);
        }
        return transactionDTOList;
    }
}
