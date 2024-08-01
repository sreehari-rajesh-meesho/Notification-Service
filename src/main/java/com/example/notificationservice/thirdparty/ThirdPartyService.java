package com.example.notificationservice.thirdparty;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class ThirdPartyService {

        private RestTemplate restTemplate;

        public ResponseEntity<ThirdPartyResponseBody> sendSMS(Long messageId, String phoneNumber, String message) {

                SMSObject smsObject = new SMSObject(message);
                Channel channel = new Channel(smsObject);
                DestinationObject destinationObject = new DestinationObject(
                        List.of(phoneNumber),
                        messageId.toString()
                );

                ThirdPartyRequest thirdPartyRequest = new ThirdPartyRequest(
                                "sms",
                                channel,
                                List.of(destinationObject)
                );

                String url = "https://api.imiconnect.in/resources/v1/messaging";

                String API_KEY = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";

                HttpHeaders headers = new HttpHeaders();

                headers.set("Key", API_KEY);

                HttpEntity<ThirdPartyRequest> request = new HttpEntity<>(thirdPartyRequest, headers);

                ResponseEntity<ThirdPartyResponseBody> response= restTemplate.exchange(url, HttpMethod.POST, request, ThirdPartyResponseBody.class);

                return response;

        }
}
