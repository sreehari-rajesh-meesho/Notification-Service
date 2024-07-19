package com.example.notificationservice.message;

import com.example.notificationservice.kafka.KafkaProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.notificationservice.constants.Constants.*;


@AllArgsConstructor
@Service
public class MessageService {


        private final MessageRepository messageRepository;
        private final KafkaProducer kafkaProducer;

        public Integer IngestMessageToDatabase(Message message) {

                Integer phoneNumber = message.getPhone_number();
                String messageText = message.getMessage();

                if(phoneNumber == null || messageText == null) {
                        return PHONE_NUMBER_MANDATORY;
                }

                messageRepository.save(message);

                return INGESTION_SUCCESSFUL;
        }

        public Integer UpdateMessageInDatabase(Long message_id, Integer status, String failure_code, String failure_comments) {

                Optional<Message> message = messageRepository.findById(message_id);

                if(message.isPresent()) {
                        message.get().setStatus(status);
                        message.get().setFailure_code(failure_code);
                        message.get().setFailure_comments(failure_comments);
                        messageRepository.save(message.get());
                        return UPDATE_SUCCESSFUL;
                }
                else {
                        return MESSAGE_WITH_ID_NOT_FOUND;
                }
        }

        public Integer PublishMessageToStream(Long message_id) {
                kafkaProducer.PublishMessageId(SEND_SMS, message_id);
                return KAFKA_INGESTION_SUCCESSFUL;
        }
}
