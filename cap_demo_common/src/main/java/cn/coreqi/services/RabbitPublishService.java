package cn.coreqi.services;

import cn.coreqi.entities.RabbitPublished;

import java.util.List;

public interface RabbitPublishService {
    void insertRabbitPublish(RabbitPublished log);
    void updateReSendMessage(String id);
    void updateMessageLogStatus(String id, String status);
    List<RabbitPublished> listStatusAndTimeoutMessage();
}
