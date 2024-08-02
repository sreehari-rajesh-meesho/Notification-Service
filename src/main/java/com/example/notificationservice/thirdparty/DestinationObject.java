package com.example.notificationservice.thirdparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class DestinationObject {
        private List<String> msisdn;
        private String correlationId;
}

