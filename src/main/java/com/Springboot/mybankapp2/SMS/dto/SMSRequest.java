package com.Springboot.mybankapp2.SMS.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSRequest {
    private String destinationSmsNumber;
    private String smsMessage;
}
