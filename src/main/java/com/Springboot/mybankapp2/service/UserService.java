package com.Springboot.mybankapp2.service;

import com.Springboot.mybankapp2.dto.TransactionRequest;
import com.Springboot.mybankapp2.dto.Response;
import com.Springboot.mybankapp2.dto.TransferRequest;
import com.Springboot.mybankapp2.dto.UserRequest;

import java.util.List;

public interface UserService {
    Response registerUser(UserRequest userRequest);
    List<Response> getAllUsers();
    Response fetchUser(Long userId);
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);

    Response transfer(TransferRequest transferRequest);

    //name enquiry
    //credit
    //debit
    //transfer
}
