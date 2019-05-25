package cn.coreqi.constants;

/**
 * 存放RabbitMq相关常量
 * @author fanqi
 */
public interface RabbitMqConstants {

    //消息队列配置
    String RABBIT_EXCHANGE = "msg-exchange";
    String RABBIT_QUEUE = "msg-add-queue";
    String RABBIT_QUEUE_ROUTINGKEY = "msg-add";

    //消息状态
    String MSG_SEND_SUCCESS = "Succeeded";
    String MSG_SEND_POST = "Posting";
    String MSG_SEND_FAIL = "Failed";

    //死信队列配置
    String RABBIT_DEADLETTER_EXCHANGE = "msg-deadletter-exchange";
    String RABBIT_DEADLETTER_QUEUE = "msg-deadletter-queue";
    String RABBIT_DEADLETTER_ROUTINGKEY = "msg-deadletter-routingkey";
}
