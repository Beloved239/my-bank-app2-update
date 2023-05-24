package com.Springboot.mybankapp2.service.Impl;

import com.Springboot.mybankapp2.Entity.User;
import com.Springboot.mybankapp2.dto.*;
import com.Springboot.mybankapp2.repository.UserRepository;
import com.Springboot.mybankapp2.service.TransactionService;
import com.Springboot.mybankapp2.service.UserService;
import com.Springboot.mybankapp2.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;

    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService){
        this.userRepository =userRepository;
        this.transactionService = transactionService;
    }
    @Override
    public Response registerUser(UserRequest userRequest) {
        /*
        check if user exists
        if user doesn't exist return response\ return response, save user and generate account number
         */
        Response response = new Response();
        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            //            response.setResponseCode();
//            response.setResponseMessage();
//            response.setData(null);
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();
        }
            User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                    .accountBalance(userRequest.getAccountBalance())
                    .email(userRequest.getEmail())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                    .status("ACTIVE")
                    .dateOfBirth(userRequest.getDateOfBirth())
                    .build();
            User savedUser = userRepository.save(user);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() +" "+ savedUser.getLastName()+" "+savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public List<Response> getAllUsers() {
        List<User> userList = userRepository.findAll();

        List<Response> responses = new ArrayList<>();

        for (User user: userList){
            responses.add(Response.builder()
                            .responseCode(ResponseUtils.SUCCESS)
                            .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                            .data(Data.builder()
                                    .accountName(user.getFirstName()+" "+ user.getLastName()+" "+ user.getOtherName())
                                    .accountNumber(user.getAccountNumber())
                                    .accountBalance(user.getAccountBalance())
                                    .build())
                    .build());
        }
        return responses;
    }
    @Override
    public Response fetchUser(Long userId) {
        if (!userRepository.existsById(userId)){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findById(userId).get();
        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName()+" "+user.getLastName()+" "+ user.getOtherName())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }
    @Override
    public Response balanceEnquiry(String accountNumber) {
        boolean isAccountNumberExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountNumberExist){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getFirstName()+" "+user.getLastName()+" "+ user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }
    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountNumberExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountNumberExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(null)
                        .accountBalance(null)
                        .build())
                .build();

    }
    @Override
    public Response credit(TransactionRequest transactionRequest) {
        User receivingUser= userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(receivingUser);
        TransactionDTO transactionDTO =new TransactionDTO();
        transactionDTO.setTransactionType("credit");
        transactionDTO.setAmount(transactionRequest.getAmount());
        transactionDTO.setAccountNumber(transactionRequest.getAccountNumber());

        transactionService.saveTransaction(transactionDTO);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .data(Data.builder()
                        .accountNumber(receivingUser.getAccountNumber())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountName(receivingUser.getFirstName()+" "+ receivingUser.getLastName()+ " "+ receivingUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response debit(TransactionRequest transactionRequest) {
        User debitinguser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        if (debitinguser.getAccountBalance().compareTo(transactionRequest.getAmount())> 0) {
            debitinguser.setAccountBalance(debitinguser.getAccountBalance().subtract(transactionRequest.getAmount()));
            userRepository.save(debitinguser);
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionType("Debit");
            transactionDTO.setAmount(transactionRequest.getAmount());
            transactionDTO.setAccountNumber(transactionRequest.getAccountNumber());
            transactionService.saveTransaction(transactionDTO);
            return Response.builder()
                    .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                    .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                    .data(Data.builder()
                            .accountNumber(debitinguser.getAccountNumber())
                            .accountBalance(debitinguser.getAccountBalance())
                            .accountName(debitinguser.getFirstName() + " " + debitinguser.getLastName() + " " + debitinguser.getOtherName())
                            .build())
                    .build();
        }
        else return Response.builder()
                .responseCode(ResponseUtils.USER_BALANCE_INSUFFICIENT)
                .responseMessage(ResponseUtils.USER_BALANCE_INSUFFICIENT_MESSAGE)
                .data(null)
                .build();
    }

    @Override
    public Response transfer(TransferRequest transferRequest) {
        User debitAccount = userRepository.findByAccountNumber(transferRequest.getSenderAccountNumber());
        User creditAccount = userRepository.findByAccountNumber(transferRequest.getReceiverAccountNumber());
        if (!userRepository.existsByAccountNumber(transferRequest.getSenderAccountNumber())){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        debitAccount.setAccountBalance(debitAccount.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(debitAccount);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountNumber(transactionDTO.getAccountNumber());
        transactionDTO.setTransactionType("Debit");
        transactionDTO.setAccountNumber(transferRequest.getSenderAccountNumber());
        transactionService.saveTransaction(transactionDTO);
        creditAccount.setAccountBalance(creditAccount.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(creditAccount);
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setAccountNumber(transferRequest.getReceiverAccountNumber());
        transactionDTO1.setTransactionType("Credit");
        transactionDTO1.setAmount(transferRequest.getAmount());
        transactionService.saveTransaction(transactionDTO1);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.SUCCESSFUL_TRANSFER_MESSAGE)
                .data(Data.builder()
                        .accountNumber(transferRequest.getReceiverAccountNumber())
                        .accountBalance(creditAccount.getAccountBalance())
                        .accountName(creditAccount.getFirstName()+" "+ creditAccount.getLastName()+" "+ creditAccount.getLastName())
                        .build())
                .build();
    }
}
