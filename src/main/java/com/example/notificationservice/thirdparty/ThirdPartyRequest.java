package com.example.notificationservice.thirdparty;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ThirdPartyRequest {
    private String deliverychannel;
    private String correlationid;
    private String notifyurl;
    private String callbackData;
    private String expiry;
    private JSONObject message;
    private JSONArray destination;
    private JSONObject channels;
}
