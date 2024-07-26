package com.example.notificationservice.thirdparty;

import com.example.notificationservice.message.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ThirdPartyService {

        public void PostDataToMessageAPI(Message message) {

                String url = "https://api.imiconnect.in/resources/v1/messaging";
                ThirdPartyRequest third_party_request = new ThirdPartyRequest();

                JSONObject channels = new JSONObject();
                JSONObject sms = new JSONObject();
                sms.put("text", message.getMessage());
                channels.put("channels", sms);

                JSONArray destination = new JSONArray();
                JSONObject element = new JSONObject();
                JSONArray phoneList = new JSONArray();
                phoneList.put(message.getPhone_number());
                element.put("correlationId", message.getId());
                destination.put(element);

                third_party_request.setDeliverychannel("sms");
                third_party_request.setChannels(channels);
                third_party_request.setDestination(destination);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForEntity(url, third_party_request, String.class);

        }
}
