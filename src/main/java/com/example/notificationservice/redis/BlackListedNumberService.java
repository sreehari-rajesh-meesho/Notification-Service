package com.example.notificationservice.redis;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BlackListedNumberService {

    private final BlackListedNumberRepository blackListedNumberRepository;

    public BlackListedNumber saveBlackListedNumber(BlackListedNumber blackListedNumber) {
        return blackListedNumberRepository.save(blackListedNumber);
    }

    public Boolean checkIfBlackListedNumber(BlackListedNumber blackListedNumber) {
        Optional<BlackListedNumber>blNum = blackListedNumberRepository.findById(blackListedNumber.getPhoneNumber());
        return blNum.isPresent();
    }

    public List<BlackListedNumber> getAllBlackListedNumbers() {

         List<BlackListedNumber> blackListedNumberList = new ArrayList<>();

        for (BlackListedNumber blackListedNumber : blackListedNumberRepository.findAll())
            blackListedNumberList.add(blackListedNumber);

         return blackListedNumberList;
    }

    public String deleteBlackListedNumber(BlackListedNumber blackListedNumber) {
        blackListedNumberRepository.delete(blackListedNumber);
        return "Deleted blacklisted number " + blackListedNumber.getPhoneNumber();
    }
}

