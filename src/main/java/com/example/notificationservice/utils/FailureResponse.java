package com.example.notificationservice.utils;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FailureResponse<T> {
    private T error;
}
