package com.xxw.shop.stream.consume;

import cn.hutool.json.JSONUtil;
import com.xxw.shop.service.SkuStockLockService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
public class RocketmqReceive {

    @Resource
    private SkuStockLockService skuStockLockService;

    @Bean
    public Consumer<List<Long>> stockUnlock() {
        return message -> {
            log.info("stockUnlock 接收 orderIds：{}", JSONUtil.toJsonStr(message));
            skuStockLockService.stockUnlock(message);
        };
    }

    @Bean
    public Consumer<List<Long>> orderNotifyStock() {
        return message -> {
            log.info("orderNotifyStock 接收 orderIds：{}", JSONUtil.toJsonStr(message));
            skuStockLockService.markerStockUse(message);
        };
    }
}
