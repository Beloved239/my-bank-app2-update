package com.Springboot.mybankapp2.SMS;

import com.Springboot.mybankapp2.SMS.dto.SMSRequest;
import com.Springboot.mybankapp2.SMS.service.SmsService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms/v1")
@Slf4j
public class SMSController {

    @Autowired
    SmsService smsService;
    @PostMapping("/processSMS")
    public String processSMS(@RequestBody SMSRequest request){
        return smsService.sendSMS(request.getDestinationSmsNumber(), request.getSmsMessage());
    }
}
