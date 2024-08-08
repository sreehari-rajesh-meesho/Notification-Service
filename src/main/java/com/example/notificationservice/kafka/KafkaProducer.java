package com.example.notificationservice.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {
    private KafkaTemplate<String, String> ProducerTemplate;
    public void PublishMessageId(String topic, String value) {
        ProducerTemplate.send(topic, value);
    }
}
