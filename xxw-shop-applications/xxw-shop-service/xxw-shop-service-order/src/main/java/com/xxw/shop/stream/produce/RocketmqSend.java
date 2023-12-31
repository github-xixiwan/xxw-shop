package com.xxw.shop.stream.produce;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RocketmqSend {

    @Resource
    private StreamBridge streamBridge;

    public boolean orderCancel(List<Long> orderIds) {
        log.info("orderCancel 发送 orderIds：{}", JSONUtil.toJsonStr(orderIds));
        Message<List<Long>> message = MessageBuilder.withPayload(orderIds)
                //设置延时等级1~18 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
                .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, 16).build();
        return streamBridge.send("order-cancel", message);
    }

    public boolean stockUnlock(List<Long> orderIds) {
        log.info("stockUnlock 发送 orderIds：{}", JSONUtil.toJsonStr(orderIds));
        Message<List<Long>> message = MessageBuilder.withPayload(orderIds).build();
        return streamBridge.send("stock-unlock", message);
    }

    public void orderNotifyStock(List<Long> orderIds) {
        log.info("orderNotifyStock 发送 orderIds：{}", JSONUtil.toJsonStr(orderIds));
        Message<List<Long>> message = MessageBuilder.withPayload(orderIds).build();
        streamBridge.send("order-notify-stock", message);
    }
}
