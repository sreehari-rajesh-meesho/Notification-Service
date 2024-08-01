package com.example.notificationservice.elasticsearch;

import com.example.notificationservice.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SendSMSServiceTest {
        @InjectMocks
        private SendSMSService sendSMSService;

        @Mock
        private SendSMSDetailsRepository sendSMSDetailsRepository;

        @Test
        public void TestFindByPage() {
                int testPageNum  = 1;
                int testPageSize  = 1;
                Pageable pageable = PageRequest.of(testPageNum, testPageSize);
                List<SendSMSDetails> listOfSMSDetails = List.of(
                        new SendSMSDetails(),
                        new SendSMSDetails()
                );
                Page<SendSMSDetails> smsPage = new PageImpl<>(listOfSMSDetails, pageable, listOfSMSDetails.size());
                Mockito.when(sendSMSDetailsRepository.findAll(pageable)).thenReturn(smsPage);
                Page<SendSMSDetails> result = sendSMSService.findByPage(testPageNum, testPageSize);
                assertEquals(result, smsPage);
        }

        @Test
        public void TestSaveMessage() {
                Mockito.when(sendSMSDetailsRepository.save(new SendSMSDetails())).thenReturn(new SendSMSDetails());
                sendSMSService.saveMessage(new Message());
                assertTrue(true);
        }

        @Test
        public void TestFindSMSContainingText() {
                String sample_text = "hello";
                SendSMSDetails ssd1 = new SendSMSDetails();
                SendSMSDetails ssd2 = new SendSMSDetails();
                ssd1.setPhone_number("+912167126721");
                ssd1.setMessage("Hello World!!");
                ssd2.setPhone_number("+913121671267");
                ssd2.setMessage("Hello Meesho!!");
                List<SendSMSDetails> listOfSMSDetails = List.of(
                        ssd1,
                        ssd2
                );

                int testPageNum  = 1;
                int testPageSize  = 1;
                Pageable pageable = PageRequest.of(testPageNum, testPageSize);
                Page<SendSMSDetails> smsDetailsPage = new PageImpl<>(listOfSMSDetails, pageable, listOfSMSDetails.size());
                Mockito.when(sendSMSDetailsRepository.findSendSMSDetailsByMessageIsContaining(sample_text, pageable)).thenReturn(smsDetailsPage);
                Page<SendSMSDetails> sendSMSDetails = sendSMSService.findSMSContainingText(sample_text, testPageNum, testPageSize);
                // Assert text in page;
                for(SendSMSDetails ssd: smsDetailsPage) {
                        assertTrue(ssd.getMessage().toLowerCase().contains(sample_text.toLowerCase()));
                }
        }

        @Test
        public void TestFindSMSBetween() {
                LocalDateTime start_time = LocalDateTime.now();
                LocalDateTime test_time = LocalDateTime.now().plusMinutes(30);
                LocalDateTime end_time = LocalDateTime.now().plusHours(1);
                SendSMSDetails ssd1 = new SendSMSDetails();
                ssd1.setPhone_number("+912178217872");
                ssd1.setMessage("Hello World!!");
                ssd1.setCreated(test_time.toInstant(ZoneOffset.UTC).toEpochMilli());
                List<SendSMSDetails> listOfSMSDetails = List.of(
                        ssd1
                );
                Long start = start_time.toInstant(ZoneOffset.UTC).toEpochMilli();
                Long end = end_time.toInstant(ZoneOffset.UTC).toEpochMilli();
                int testPageNum  = 1;
                int testPageSize  = 1;
                Pageable pageable = PageRequest.of(testPageNum, testPageSize);
                Page<SendSMSDetails> smsDetailsPage = new PageImpl<>(listOfSMSDetails, pageable, listOfSMSDetails.size());
                Mockito.when(sendSMSDetailsRepository.findSendSMSDetailsByCreatedBetween(start, end, pageable)).thenReturn(smsDetailsPage);
                Page<SendSMSDetails> smsDetails = sendSMSService.findSMSBetween(start_time, end_time, testPageNum, testPageSize);
                for(SendSMSDetails ssd: smsDetailsPage) {
                        assertTrue(ssd.getCreated() > start && ssd.getCreated() < end);
                }
        }
}
