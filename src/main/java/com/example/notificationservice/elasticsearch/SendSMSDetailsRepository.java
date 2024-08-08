package com.example.notificationservice.elasticsearch;

import org.apache.kafka.common.network.Send;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SendSMSDetailsRepository extends ElasticsearchRepository<SendSMSDetails, String> {
        Page<SendSMSDetails> findAll(Pageable pageable);
        Page<SendSMSDetails> findSendSMSDetailsByCreatedBetweenAndPhoneNumber(Long start, Long end, String phoneNumber, Pageable pageable);
}
