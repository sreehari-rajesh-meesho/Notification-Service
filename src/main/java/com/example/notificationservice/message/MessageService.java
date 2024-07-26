package com.example.notificationservice.message;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.notificationservice.constants.Constants.*;

import java.util.Optional;

import static com.example.notificationservice.constants.Constants.*;


@AllArgsConstructor
@Service
public class MessageService {


        private final MessageRepository messageRepository;


        public Long IngestMessageToDatabase(Message message) {

                Integer phoneNumber = message.getPhone_number();
                String messageText = message.getMessage();

                if(phoneNumber == null) {
                        return PHONE_NUMBER_MANDATORY;
                }

                Message savedMessage = messageRepository.save(message);
                Long messageId = savedMessage.getId();
                return messageId;
        }

        public Long UpdateMessageInDatabase(Long message_id, Integer status, String failure_code, String failure_comments) {

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
}
