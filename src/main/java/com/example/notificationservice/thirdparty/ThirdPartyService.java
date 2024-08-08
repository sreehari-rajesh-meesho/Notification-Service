package com.example.notificationservice.thirdparty;

import com.meesho.instrumentation.annotation.DigestLogger;
import com.meesho.instrumentation.enums.MetricType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static com.example.notificationservice.utils.Constants.*;

@Service
@AllArgsConstructor
public class ThirdPartyService {

        private RestTemplate restTemplate;

        @DigestLogger(metricType = MetricType.HTTP, tagSet = "api=Third Party SMS API")
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

                HttpHeaders headers = new HttpHeaders();

                headers.set("Key", API_KEY);

                HttpEntity<ThirdPartyRequest> request = new HttpEntity<>(thirdPartyRequest, headers);
                ResponseEntity<ThirdPartyResponseBody> response;
                try {
                        return restTemplate.exchange(API_URL, HttpMethod.POST, request, ThirdPartyResponseBody.class);
                } catch (HttpStatusCodeException e) {
                        return ResponseEntity.status(e.getStatusCode()).body(null);
                }
        }
}
