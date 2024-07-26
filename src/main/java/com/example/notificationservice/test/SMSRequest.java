package com.example.notificationservice.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SMSRequest {
        private Long phoneNumber;
        private String message;
}
