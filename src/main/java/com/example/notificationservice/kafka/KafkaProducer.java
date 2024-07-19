package com.example.notificationservice.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class KafkaProducer {

    private KafkaTemplate<String, Long> ProducerTemplate;

    public void PublishMessageId(String topic, Long value) {
        ProducerTemplate.send(topic, value);
    }
}
