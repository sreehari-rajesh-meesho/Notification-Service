package com.example.notificationservice.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

        // Success Codes
        private static final int INGESTION_SUCCESSFUL = 0;
        private static final int UPDATE_SUCCESSFUL = 0;

        // Failure Codes
        private static final int PHONE_NUMBER_MANDATORY = -1;
        private static final int MESSAGE_WITH_ID_NOT_FOUND = -2;

        private final MessageRepository messageRepository;

        @Autowired
        public MessageService(MessageRepository messageRepository) {
                this.messageRepository = messageRepository;
        }

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
                } else {
                        return MESSAGE_WITH_ID_NOT_FOUND;
                }
        }
}
