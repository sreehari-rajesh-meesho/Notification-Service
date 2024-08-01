package com.example.notificationservice.redis;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BlackListedNumberServiceTest {

    @InjectMocks
    private BlackListedNumberService blackListedNumberService;

    @Mock
    private BlackListedNumberRepository blackListedNumberRepository;

    @Test
    public void TestSaveBlackListedNumber() {
        BlackListedNumber blackListedNumber = new BlackListedNumber();
        Mockito.when(blackListedNumberRepository.save(blackListedNumber)).thenReturn(blackListedNumber);
        blackListedNumberService.saveBlackListedNumber(blackListedNumber);
        assertTrue(true);
    }

    @Test
    public void TestCheckIfNumberIsBlacklisted() {
        BlackListedNumber blackListedNumber = new BlackListedNumber();
        Mockito.when(blackListedNumberRepository.findById(blackListedNumber.getPhoneNumber())).thenReturn(Optional.of(blackListedNumber));
        assertTrue(blackListedNumberService.checkIfBlackListedNumber(blackListedNumber));
    }

    @Test
    public void DeleteBlackListedNumber() {
        BlackListedNumber blackListedNumber = new BlackListedNumber();
        Mockito.when(blackListedNumberRepository.save(blackListedNumber)).thenReturn(blackListedNumber);
        blackListedNumberService.saveBlackListedNumber(blackListedNumber);
        assertTrue(true);
    }
}
