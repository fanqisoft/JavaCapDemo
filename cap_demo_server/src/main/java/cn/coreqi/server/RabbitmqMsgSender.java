package cn.coreqi.server;

import cn.coreqi.constants.RabbitMqConstants;
import cn.coreqi.entities.CoreqiMessage;
import cn.coreqi.entities.RabbitPublished;
import cn.coreqi.services.RabbitPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class RabbitmqMsgSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitPublishService rabbitPublishService;

    // 消息发送到交换器Exchange后触发回调
    private final RabbitTemplate.ConfirmCallback confirmCallback =new RabbitTemplate.ConfirmCallback() {

        /**
         *
         * @param correlationData   回调的相关数据
         * @param ack   是否确认
         * @param cause 一个可选的原因，对于nack，如果可用，否则为空。
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("生产端confirm...");
            log.info("correlationData=" + correlationData);
            String messageId = correlationData.getId();
            if (ack) {
                //confirm返回成功,更新消息投递状态
                rabbitPublishService.updateMessageLogStatus(messageId, RabbitMqConstants.MSG_SEND_SUCCESS);
            } else {
                // 失败则进行具体的后续操作，重试或者补偿等手段。
                log.info("异常处理...");
            }
        }};

    // 如果消息从交换器发送到对应队列失败时触发
    private final RabbitTemplate.ReturnCallback returnCallback =new RabbitTemplate.ReturnCallback() {

        /**
         *
         * @param message   返回的消息
         * @param replyCode 返回代码
         * @param replyText 返回内容
         * @param exchange  交换机名称
         * @param routingKey    routingKey
         */
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.info("message=" + message.toString());
            log.info("replyCode=" + replyCode);
            log.info("replyText=" + replyText);
            log.info("exchange=" + exchange);
            log.info("routingKey=" + routingKey);
        }};

    public void sendMsg(CoreqiMessage msg) {
        log.info("生产端发送消息...");
        rabbitTemplate.setConfirmCallback(this.confirmCallback);
        rabbitTemplate.setReturnCallback(this.returnCallback);
        CorrelationData correlationData = new CorrelationData(msg.getMessageId());
        try {
            rabbitTemplate.convertAndSend(RabbitMqConstants.RABBIT_EXCHANGE,RabbitMqConstants.RABBIT_QUEUE_ROUTINGKEY, msg, correlationData);
            LocalDateTime dateTime = LocalDateTime.now();
            LocalDateTime exp = dateTime.plusDays(1);
            RabbitPublished log = new RabbitPublished(msg.getMessageId(),null,msg.getMessage().toString(),1,dateTime,dateTime,exp,RabbitMqConstants.MSG_SEND_POST);
            rabbitPublishService.InsertRabbitPublish(log);
        }
        catch (Exception e){
            //回滚事务
        }
    }
}
