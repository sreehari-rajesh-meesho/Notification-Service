package com.example.notificationservice.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SendSMSDetailsRepository extends ElasticsearchRepository<SendSMSDetails, String> {
        Page<SendSMSDetails> findAll(Pageable pageable);
        Page<SendSMSDetails> findByMessageContaining(String text, Pageable pageable);
        Page<SendSMSDetails> findByCreatedBetween(Long start, Long end, Pageable pageable);
}
