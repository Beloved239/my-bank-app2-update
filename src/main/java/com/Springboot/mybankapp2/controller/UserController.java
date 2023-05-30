package com.Springboot.mybankapp2.controller;

import com.Springboot.mybankapp2.Entity.Transaction;
import com.Springboot.mybankapp2.dto.*;
import com.Springboot.mybankapp2.service.TransactionService;
import com.Springboot.mybankapp2.service.UserService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Banking Application",
                description = "Spring Book Banking Application REST APIs Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Oluwatobi",
                        email = "adettob@gmail.com",
                        url = "https://github.com/beloved239"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/Beloved239/my-bank-app2"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Banking Implementation",
                url = "https://github.com/Beloved239/my-bank-app2"
        )
)
@Tag(
        name = "User Account Service REST APIs?Endpoint",
        description = "Endpoints for Manipulating User Account"
)
public class UserController {
    private final UserService userService;
    private final TransactionService transactionService;
    public UserController (UserService userService, TransactionService transactionService){
        this.userService=userService;
        this.transactionService=transactionService;
    }

    @PostMapping("/registerUser")
    public Response registerUser(@RequestBody UserRequest userRequest){
        return userService.registerUser(userRequest);
    }
    @GetMapping("/getAllUsers")
    public List<Response> fetchAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/getAllTransactions")
    public List<TransactionDTO> fetchAllTransaction(){
    return transactionService.getAllTransaction();
    }

    @GetMapping("/getUserById{id}")
    public Response getUserById(@PathVariable(name = "id") Long userId){
        return userService.fetchUser(userId);
    }

    @GetMapping("/balanceEnquiry/accountNumber")
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
