package cn.coreqi.config;

import cn.coreqi.constants.RabbitMqConstants;
import cn.coreqi.entities.CoreqiMessage;
import cn.coreqi.entities.RabbitPublished;
import cn.coreqi.services.RabbitPublishService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 执行重新投递status为0的消息
 */
@Component
@Slf4j
public class RetryMessageTask {

    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitPublishService rabbitPublishService;

    @Scheduled(initialDelay = 5000, fixedDelay = 30000)
    public void trySendMessage() {
        log.info("定时投递status为0的消息...");
        List<RabbitPublished> rabbitPublishedList = rabbitPublishService.listStatusAndTimeoutMessage();
        rabbitPublishedList.forEach(rabbitPublished -> {
            if (rabbitPublished.getRetries() >= 3) {
                log.info("投递3次还是失败...");
                //更新状态为失败
                rabbitPublishService.updateMessageLogStatus(rabbitPublished.getId(), RabbitMqConstants.MSG_SEND_FAIL);
                //将消息打入死信队列
            }
            else {
                log.info("重新投递...");
                rabbitPublishService.updateReSendMessage(rabbitPublished.getId());
                CorrelationData correlationData = new CorrelationData(rabbitPublished.getId());
                CoreqiMessage msg = new CoreqiMessage(rabbitPublished.getId(),200,rabbitPublished.getContent(),"success");
                try {
                    rabbitTemplate.convertAndSend(RabbitMqConstants.RABBIT_EXCHANGE,RabbitMqConstants.RABBIT_QUEUE_ROUTINGKEY,msg,correlationData);
                } catch (Exception e) {
                    log.error("重新投递消息发送异常...:" + e.getMessage());
                }
            }
        });
    }
}
