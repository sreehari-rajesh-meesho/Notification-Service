package com.example.notificationservice.thirdparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyRequest {
    private String deliverychannel;
    private Channel channels;
    private List<DestinationObject> destination;
}
