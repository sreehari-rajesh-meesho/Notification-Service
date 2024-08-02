package com.example.notificationservice.message;

import com.meesho.instrumentation.annotation.DigestLogger;
import com.meesho.instrumentation.enums.MetricType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notificationservice.utils.Constants.*;


@AllArgsConstructor
@Service
public class MessageService {

        private final MessageRepository messageRepository;

        @DigestLogger(metricType = MetricType.RDS, tagSet = "api=SQL")
        public Long IngestMessageToDatabase(Message message) {

                String phoneNumber = message.getPhone_number();

                if(phoneNumber == null) {
                        return PHONE_NUMBER_MANDATORY;
                }

                message.setCreatedAt(LocalDateTime.now());

                try {
                        Message savedMessage = messageRepository.save(message);
                        return savedMessage.getId();
                } catch (Exception e) {
                        return DATABASE_ERROR;
                }
        }

        @DigestLogger(metricType = MetricType.RDS, tagSet = "api=SQL")
        public Optional<Message> findMessageById(Long message_id) {
                return messageRepository.findById(message_id);
        }

        @DigestLogger(metricType = MetricType.RDS, tagSet = "api=SQL")
        public Long UpdateMessageInDatabase(Long message_id, Integer status, String failure_code, String failure_comments) {

                try {
                        Optional<Message> message = messageRepository.findById(message_id);

                        if(message.isPresent()) {
                                message.get().setStatus(status);
                                message.get().setFailure_code(failure_code);
                                message.get().setFailure_comments(failure_comments);
                                message.get().setUpdatedAt(LocalDateTime.now());
                                return messageRepository.save(message.get()).getId();
                        }
                        else {
                                return MESSAGE_WITH_ID_NOT_FOUND;
                        }
                } catch (Exception e) {
                        return DATABASE_ERROR;
                }
        }
}
