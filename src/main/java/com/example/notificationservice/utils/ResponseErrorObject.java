package com.example.notificationservice.utils;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseErrorObject {
        private String code;
        private String message;
}
