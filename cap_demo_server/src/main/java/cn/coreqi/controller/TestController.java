package cn.coreqi.controller;

import cn.coreqi.entities.CoreqiMessage;
import cn.coreqi.server.RabbitmqMsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private RabbitmqMsgSender sender;

    @GetMapping("/test")
    public String test(){
        CoreqiMessage msg = new CoreqiMessage(UUID.randomUUID().toString(),200,"测试内容240306178","success");
        sender.sendMsg(msg);
        return "seccess";
    }
}
