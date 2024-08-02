package com.example.notificationservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDataObject {
    private Long requestId;
    private String comments;
}
