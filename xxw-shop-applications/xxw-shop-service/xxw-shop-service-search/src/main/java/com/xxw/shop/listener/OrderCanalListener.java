package com.xxw.shop.listener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.xxw.shop.api.order.feign.OrderFeignClient;
import com.xxw.shop.bo.OrderInfoBO;
import com.xxw.shop.constant.EsIndexEnum;
import com.xxw.shop.constant.SearchBusinessError;
import com.xxw.shop.module.common.bo.EsOrderBO;
import com.xxw.shop.module.common.exception.BusinessException;
import com.xxw.shop.module.common.response.ServerResponseEntity;
import com.xxw.shop.starter.canal.model.CanalBinLogResult;
import com.xxw.shop.starter.canal.support.processor.BaseCanalBinlogEventProcessor;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderCanalListener extends BaseCanalBinlogEventProcessor<OrderInfoBO> {

    private static final Logger log = LoggerFactory.getLogger(OrderCanalListener.class);

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private ElasticsearchClient client;

    /**
     * 插入订单，此时插入es
     */
    @Override
    protected void processInsertInternal(CanalBinLogResult<OrderInfoBO> result) {
        Long orderId = result.getPrimaryKey();
        ServerResponseEntity<EsOrderBO> serverResponseEntity = orderFeignClient.getEsOrder(orderId);
        try {
            IndexResponse indexResponse = client.index(i ->
                    // 索引
                    i.index(EsIndexEnum.ORDER.value())
                            // ID
                            .id(String.valueOf(orderId))
                            // 文档
                            .document(serverResponseEntity.getData()));
            log.info("elasticsearch返回结果：" + indexResponse.toString());
        } catch (Exception e) {
            log.error("elasticsearch异常 错误：{}", ExceptionUtils.getStackTrace(e));
            throw new BusinessException(SearchBusinessError.SEARCH_00002);
        }
    }

    /**
     * 更新订单，删除订单索引，再重新构建一个
     */
    @Override
    protected void processUpdateInternal(CanalBinLogResult<OrderInfoBO> result) {
        Long orderId = result.getPrimaryKey();
        ServerResponseEntity<EsOrderBO> serverResponseEntity = orderFeignClient.getEsOrder(orderId);
        if (!serverResponseEntity.isSuccess()) {
            return;
        }
        try {
            UpdateResponse<EsOrderBO> updateResponse = client.update(u ->
                    // 索引
                    u.index(EsIndexEnum.ORDER.value())
                            // ID
                            .id(String.valueOf(orderId))
                            // 文档
                            .doc(serverResponseEntity.getData()), EsOrderBO.class);
            log.info("elasticsearch返回结果：" + updateResponse.toString());
        } catch (Exception e) {
            log.error("elasticsearch异常 错误：{}", ExceptionUtils.getStackTrace(e));
        }
    }
}
