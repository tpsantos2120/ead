package com.ead.authuser.publishers;

import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.enums.ActionType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private String exchangeUserEvent;

    public void publishUserEvent(UserEventDto userEventDto, ActionType actionType) {
        userEventDto.setActionType(actionType.name());
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEventDto);
    }
}
