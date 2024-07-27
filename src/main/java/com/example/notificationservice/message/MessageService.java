package com.example.notificationservice.message;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.notificationservice.constants.Constants.*;


@AllArgsConstructor
@Service
public class MessageService {

        private final MessageRepository messageRepository;

        public Integer IngestMessageToDatabase(Message message) {

                String phoneNumber = message.getPhone_number();
                String messageText = message.getMessage();

                if(phoneNumber == null || messageText == null) {
                        return PHONE_NUMBER_MANDATORY;
                }

                Message savedMessage = messageRepository.save(message);
                Integer messageId = Math.toIntExact(savedMessage.getId());

                return messageId;
        }

        public Integer UpdateMessageInDatabase(Long message_id, Integer status, String failure_code, String failure_comments) {

                Optional<Message> message = messageRepository.findById(message_id);

                if(message.isPresent()) {

                        message.get().setStatus(status);
                        message.get().setFailure_code(failure_code);
                        message.get().setFailure_comments(failure_comments);

                        Message updatedMessage = messageRepository.save(message.get());
                        Integer messageId = Math.toIntExact(updatedMessage.getId());

                        return messageId;
                }
                else {
                        return MESSAGE_WITH_ID_NOT_FOUND;
                }
        }
}
