package com.Springboot.mybankapp2.email.service;

import com.Springboot.mybankapp2.email.dto.EmailDetails;

public interface EmailService {
    String sendSimpleEmail(EmailDetails emailDetails);

    String sendEmailWithAttachment(EmailDetails emailDetails);
}
