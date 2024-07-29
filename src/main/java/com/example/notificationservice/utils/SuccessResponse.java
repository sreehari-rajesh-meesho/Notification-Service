package com.example.notificationservice.utils;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuccessResponse<T> {

    private T data;

}
