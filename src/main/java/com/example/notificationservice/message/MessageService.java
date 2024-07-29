package com.example.notificationservice.message;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notificationservice.constants.Constants.*;


@AllArgsConstructor
@Service
public class MessageService {

        private final MessageRepository messageRepository;

        public Long IngestMessageToDatabase(Message message) {

                String phoneNumber = message.getPhone_number();
                String messageText = message.getMessage();

                if(phoneNumber == null) {
                        return PHONE_NUMBER_MANDATORY;
                }

                message.setCreated_at(LocalDateTime.now());

                try {
                        Message savedMessage = messageRepository.save(message);
                        Long messageId = savedMessage.getId();
                        return messageId;

                } catch (Exception e) {
                        return DATABASE_ERROR;
                }
        }

        public Long UpdateMessageInDatabase(Long message_id, Integer status, String failure_code, String failure_comments) {

                try {
                        Optional<Message> message = messageRepository.findById(message_id);

                        if(message.isPresent()) {

                                message.get().setStatus(status);
                                message.get().setFailure_code(failure_code);
                                message.get().setFailure_comments(failure_comments);
                                message.get().setUpdated_at(LocalDateTime.now());

                                messageRepository.save(message.get());

                                return UPDATE_SUCCESSFUL;
                        }
                        else {
                                return MESSAGE_WITH_ID_NOT_FOUND;
                        }
                } catch (Exception e) {
                        return DATABASE_ERROR;
                }
        }
}
