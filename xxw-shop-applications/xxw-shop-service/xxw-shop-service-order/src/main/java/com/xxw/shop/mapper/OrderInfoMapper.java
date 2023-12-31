package com.xxw.shop.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xxw.shop.api.order.vo.*;
import com.xxw.shop.entity.OrderInfo;
import com.xxw.shop.module.common.bo.EsOrderBO;
import com.xxw.shop.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 映射层。
 *
 * @author liaoxiting
 * @since 2023-08-10
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 获取订单和订单项的数据
     *
     * @param orderId
     * @param shopId
     * @return
     */
    OrderCompleteVO getOrderAndOrderItemData(@Param("orderId") Long orderId, @Param("shopId") Long shopId);

    /**
     * 计算每个订单状态的状态数量
     *
     * @param userId 用户id
     * @return
     */
    OrderCountVO countNumberOfStatus(@Param("userId") Long userId);

    /**
     * 查询订单状态
     *
     * @param orderIds 多个订单的订单id
     * @return 订单状态列表
     */
    List<OrderStatusVO> getOrdersStatus(@Param("orderIds") List<Long> orderIds);

    /**
     * 取消订单
     *
     * @param orderIds 订单ids
     */
    void cancelOrders(@Param("orderIds") List<Long> orderIds);

    /**
     * 确认收货
     *
     * @param orderId
     * @return
     */
    int receiptOrder(@Param("orderId") Long orderId);

    /**
     * 根据订单号删除订单
     *
     * @param orderId 订单号
     */
    void deleteOrder(@Param("orderId") Long orderId);

    /**
     * 根据订单id列表获取订单金额信息
     *
     * @param orderIdList 订单id列表
     * @return
     */
    SubmitOrderPayAmountInfoVO getSubmitOrderPayAmountInfo(@Param("orderIdList") long[] orderIdList);

    /**
     * 获取订单需要保存到es中的数据
     *
     * @param orderId 订单id
     * @return
     */
    EsOrderBO getEsOrder(@Param("orderId") Long orderId);

    /**
     * 计算订单实际金额
     *
     * @param orderIds 多个订单的订单id
     * @return 订单实际金额总和
     */
    OrderAmountVO getOrdersActualAmount(@Param("orderIds") List<Long> orderIds);

    /**
     * 将订单改为已支付状态
     *
     * @param orderIds 订单ids
     */
    void updateByToPaySuccess(@Param("orderIds") List<Long> orderIds);


    /**
     * 获取订单中的金额信息
     *
     * @param orderIds 多个订单的订单id
     * @return 获取订单中的金额信息
     */
    List<OrderSimpleAmountInfoVO> getOrdersSimpleAmountInfo(@Param("orderIds") List<Long> orderIds);
}
