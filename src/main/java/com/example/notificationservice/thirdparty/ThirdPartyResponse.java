package com.example.notificationservice.thirdparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyResponse {
        private String code;
        private String description;
        private String transid;
}
