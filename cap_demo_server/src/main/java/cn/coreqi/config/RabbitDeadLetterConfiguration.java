package cn.coreqi.config;

import cn.coreqi.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置死信队列
 */
@Configuration
public class RabbitDeadLetterConfiguration {

    @Bean(name = "deadLetterExchage")
    public Exchange deadLetterExchange() {
        return new DirectExchange(RabbitMqConstants.RABBIT_DEADLETTER_EXCHANGE, true, false);
    }

    @Bean(name = "deadQueueLetter")
    public Queue deadLetterQueue() {
        return new Queue(RabbitMqConstants.RABBIT_DEADLETTER_QUEUE, true, false, false, null);
    }

    @Bean(name = "deadLetterBinding")
    public Binding deadLetterBinding() {
        return new Binding(RabbitMqConstants.RABBIT_DEADLETTER_QUEUE, Binding.DestinationType.QUEUE,
                RabbitMqConstants.RABBIT_DEADLETTER_EXCHANGE, RabbitMqConstants.RABBIT_DEADLETTER_ROUTINGKEY, null);
    }
}
