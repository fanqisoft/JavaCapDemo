package cn.coreqi.config;

import cn.coreqi.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @PostConstruct
//    protected void init() {
//
//        //设置为支持事务
//        rabbitTemplate.setChannelTransacted(true);
//    }

    @Bean(name = "msgExchange")
    public Exchange msgExchange() {

        return new TopicExchange(RabbitMqConstants.RABBIT_EXCHANGE, true, false);
    }

    @Bean(name = "msgQueue")
    public Queue msgQueue() {

        Map<String, Object> args = new HashMap<>(2);
        //当队列的消息达到上限后溢出的消息不会被删除而是保存到另一个指定的队列中。
        args.put("x-dead-letter-exchange", RabbitMqConstants.RABBIT_DEADLETTER_EXCHANGE);
        //指定队列的routing-key
        args.put("x-dead-letter-routing-key", RabbitMqConstants.RABBIT_DEADLETTER_ROUTINGKEY);

        return new Queue(RabbitMqConstants.RABBIT_QUEUE, true, false, true, args);
    }

    @Bean(name="msgBinding")
    public Binding msgBinding(@Qualifier("msgQueue") Queue msgQueue, @Qualifier("msgExchange") Exchange msgExchange){
        return BindingBuilder.bind(msgQueue).to(msgExchange).with(RabbitMqConstants.RABBIT_QUEUE_ROUTINGKEY).noargs();
    }
}
