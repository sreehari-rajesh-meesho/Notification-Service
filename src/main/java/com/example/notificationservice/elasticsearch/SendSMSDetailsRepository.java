package com.example.notificationservice.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendSMSDetailsRepository extends ElasticsearchRepository<SendSMSDetails, String> {

}
