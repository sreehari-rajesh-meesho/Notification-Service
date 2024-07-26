package com.example.notificationservice.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SendSMSDetailsRepository extends ElasticsearchRepository<SendSMSDetails, String> {

}
