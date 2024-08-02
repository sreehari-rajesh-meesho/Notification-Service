package com.example.notificationservice.thirdparty;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.example.notificationservice.utils.Constants.API_KEY;
import static com.example.notificationservice.utils.Constants.API_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ThirdPartyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ThirdPartyService thirdPartyService;


    @Test
    public void TestSendSMS() {

            String testPhoneNumber = "218971282";
            String testMessage = "test message";
            Long messageId = 1L;

            ThirdPartyResponse thirdPartyResponse = new ThirdPartyResponse(
                    "7001",
                    "Authentication Failed",
                    "28e56-e1egf-909adghe-90ef"
            );

            ThirdPartyResponseBody thirdPartyResponseBody = new ThirdPartyResponseBody(thirdPartyResponse);
            ResponseEntity<ThirdPartyResponseBody> response = new ResponseEntity<>(thirdPartyResponseBody, HttpStatus.OK);

            Mockito.when(
                    restTemplate.exchange(
                        Mockito.anyString(),
                        Mockito.<HttpMethod>any(),
                        Mockito.<HttpEntity<ThirdPartyRequest>>any(),
                        Mockito.<Class<ThirdPartyResponseBody>>any()
                    )
            ).thenReturn(response);

            ResponseEntity<ThirdPartyResponseBody> serviceResponse = thirdPartyService.sendSMS(messageId, testPhoneNumber, testMessage);
            assertEquals(serviceResponse.getBody(), thirdPartyResponseBody);

    }

}
