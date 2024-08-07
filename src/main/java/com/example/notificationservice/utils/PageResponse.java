package com.example.notificationservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private String error;
    private Page<T> data;
}
