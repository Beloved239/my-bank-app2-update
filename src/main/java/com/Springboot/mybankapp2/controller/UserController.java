package com.Springboot.mybankapp2.controller;

import com.Springboot.mybankapp2.Entity.Transaction;
import com.Springboot.mybankapp2.dto.*;
import com.Springboot.mybankapp2.service.TransactionService;
import com.Springboot.mybankapp2.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final TransactionService transactionService;
    public UserController (UserService userService, TransactionService transactionService){
        this.userService=userService;
        this.transactionService=transactionService;
    }

    @PostMapping
    public Response registerUser(@RequestBody UserRequest userRequest){
        return userService.registerUser(userRequest);
    }
    @GetMapping
    public List<Response> fetchAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/transaction")
    public List<TransactionDTO> fetchAllTransaction(){
    return transactionService.getAllTransaction();
    }

    @GetMapping("/{id}")
    public Response getUserById(@PathVariable(name = "id") Long userId){
        return userService.fetchUser(userId);
    }

    @GetMapping("/accountNumber")
    public Response balanceEnquiry(@RequestParam(name = "accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public Response nameEnquiry(@RequestParam(name = "accountNumber")String accountNumber){
        return userService.nameEnquiry(accountNumber);
    }

    @PutMapping
    public Response creditRequest(@RequestBody TransactionRequest transactionRequest){
        return userService.credit(transactionRequest);
    }

    @PutMapping("/debit")
    public Response debitRequest(@RequestBody TransactionRequest transactionRequest){
        return userService.debit(transactionRequest);
    }

    @PutMapping("/debit/transfer")
    public Response transferRequest(@RequestBody TransferRequest transferRequest){
       return userService.transfer(transferRequest);
    }

    @GetMapping("/transaction/history")
    public Transaction getTransactionByAccountNumber(@RequestBody TransactionHistory transactionRequest){
        return transactionService.fetchByAccountNumber(transactionRequest);
    }

    @GetMapping("/single/transaction/history")
    public List<TransactionDTO> fetchTransactionsByAccountNumber(@RequestBody TransactionHistory transactionHistory){
        return transactionService.getByAccountNumber(transactionHistory);
    }

}
