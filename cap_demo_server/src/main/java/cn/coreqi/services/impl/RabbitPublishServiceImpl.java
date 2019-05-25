package cn.coreqi.services.impl;

import cn.coreqi.constants.RabbitMqConstants;
import cn.coreqi.entities.RabbitPublished;
import cn.coreqi.services.RabbitPublishService;
import cn.coreqi.repository.RabbitPublishRepository;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitPublishServiceImpl implements RabbitPublishService {

    @Autowired
    private RabbitPublishRepository repository;

    @Override
    public void insertRabbitPublish(RabbitPublished log) {
        repository.save(log);
    }

    @Override
    public void updateReSendMessage(String id) {
        RabbitPublished data = repository.findById(id).get();
        data.setRetries(data.getRetries() + 1);
        repository.save(data);
    }

    @Override
    public void updateMessageLogStatus(String id, String status) {
        RabbitPublished data = repository.findById(id).get();
        data.setStatusName(status);
        repository.save(data);
    }

    @Override
    public List<RabbitPublished> listStatusAndTimeoutMessage() {
        return repository.findByStatusName(RabbitMqConstants.MSG_SEND_POST);
    }

}
