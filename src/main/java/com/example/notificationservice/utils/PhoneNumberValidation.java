package com.example.notificationservice.utils;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.example.notificationservice.utils.Constants.INVALID_REQUEST;
import static com.example.notificationservice.utils.Constants.PHONE_NUMBER_MANDATORY;

@AllArgsConstructor
@Component
public class PhoneNumberValidation {

    public Long isValidPhoneNumber(String phoneNumber) {

        if(phoneNumber == null) {
            return PHONE_NUMBER_MANDATORY;
        }

        if(phoneNumber.isEmpty()) {
            return PHONE_NUMBER_MANDATORY;
        }

        if(phoneNumber.length() != 13) {
            return INVALID_REQUEST;
        }

        if(phoneNumber.charAt(0) != '+') {
            return INVALID_REQUEST;
        }

        if(phoneNumber.charAt(1) != '9') {
            return INVALID_REQUEST;
        }

        if(phoneNumber.charAt(2) != '1') {
            return INVALID_REQUEST;
        }

        if(!(phoneNumber.charAt(3) >= '6' && phoneNumber.charAt(3) <= '9')) {
            return INVALID_REQUEST;
        }

        for(int i = 4; i < phoneNumber.length(); i++) {
            if(!(phoneNumber.charAt(i)>='0' && phoneNumber.charAt(i)<='9')) {
                return INVALID_REQUEST;
            }
        }

        return 0L;
    }
}
