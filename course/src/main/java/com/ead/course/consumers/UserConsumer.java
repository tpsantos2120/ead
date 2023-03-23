package com.ead.course.consumers;

import com.ead.course.dtos.UserEventDto;
import com.ead.course.enums.ActionType;
import com.ead.course.mappers.UserMapper;
import com.ead.course.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConsumer {

    private final UserMapper mapper;
    private final UserService userService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "${ead.broker.queue.userEventQueue.name}",
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value = "${ead.broker.exchange.userEventExchange}",
                            type = ExchangeTypes.FANOUT,
                            ignoreDeclarationExceptions = "true"
                    )
            )
    )
    public void listenUserEvent(@Payload UserEventDto userEventDto) {
        var userModel = mapper.userEventDtoToUserModel(userEventDto);

        switch (ActionType.valueOf(userEventDto.getActionType())) {
            case CREATE, UPDATE -> userService.save(userModel);
            case DELETE -> userService.delete(userModel.getId());
        }
    }
}
