package com.example.notificationservice.elasticsearch;

import org.apache.kafka.common.network.Send;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SendSMSDetailsRepository extends ElasticsearchRepository<SendSMSDetails, String> {
        Page<SendSMSDetails> findAll(Pageable pageable);
        Page<SendSMSDetails> findSendSMSDetailsByCreatedBetween(Long start, Long end, Pageable pageable);
        Page<SendSMSDetails> findSendSMSDetailsByMessageContaining(String text, Pageable pageable);
}
