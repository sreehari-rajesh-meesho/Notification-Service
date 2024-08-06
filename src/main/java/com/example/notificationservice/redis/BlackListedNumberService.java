package com.example.notificationservice.redis;

import com.meesho.instrumentation.annotation.DigestLogger;
import com.meesho.instrumentation.enums.MetricType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BlackListedNumberService {

    private final BlackListedNumberRepository blackListedNumberRepository;

    @DigestLogger(metricType = MetricType.REDIS, tagSet = "api=REDIS")
    public void saveBlackListedNumber(BlackListedNumber blackListedNumber) {
            blackListedNumberRepository.save(blackListedNumber);
    }

    @DigestLogger(metricType = MetricType.REDIS, tagSet = "api=REDIS")
    public void saveBlackListedNumberList(Iterable<BlackListedNumber> blackList) {
        blackListedNumberRepository.saveAll(blackList);
    }

    @DigestLogger(metricType = MetricType.REDIS, tagSet = "api=REDIS")
    public Boolean checkIfBlackListedNumber(BlackListedNumber blackListedNumber) {
        Optional<BlackListedNumber>blNum = blackListedNumberRepository.findById(blackListedNumber.getPhoneNumber());
        return blNum.isPresent();
    }

    @DigestLogger(metricType = MetricType.REDIS, tagSet = "api=REDIS")
    public void deleteBlackListedNumber(BlackListedNumber blackListedNumber) {
        blackListedNumberRepository.delete(blackListedNumber);
    }

    @DigestLogger(metricType = MetricType.REDIS, tagSet = "api=REDIS")
    public void deleteAllBlackListedNumbers(Iterable<BlackListedNumber> blackListedNumbers) {
        blackListedNumberRepository.deleteAll(blackListedNumbers);
    }
}

