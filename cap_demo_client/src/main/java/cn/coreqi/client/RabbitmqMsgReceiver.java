package cn.coreqi.client;

import cn.coreqi.constants.RabbitMqConstants;
import cn.coreqi.entities.CoreqiMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 消息消费端
 */
@Component
@Slf4j
public class RabbitmqMsgReceiver {
    @RabbitListener(queues = RabbitMqConstants.RABBIT_QUEUE)
    public void receive(@Payload CoreqiMessage msg, Channel channel, @Headers Map headers, Message message) throws IOException, InterruptedException {
        log.info("消费端接收消息...");
        log.info("message=" + message.toString());
        log.info("CoreqiMessage=" + msg);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        log.info("deliveryTag=" + deliveryTag);
        channel.basicAck(deliveryTag, false);   // 手工ack
    }
}
