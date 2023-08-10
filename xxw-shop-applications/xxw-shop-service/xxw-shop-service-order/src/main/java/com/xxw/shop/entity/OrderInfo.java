package com.xxw.shop.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  实体类。
 *
 * @author liaoxiting
 * @since 2023-08-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "order_info")
public class OrderInfo implements Serializable {

    /**
     * 订单ID
     */
    @Id
    private Long orderId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 配送类型：无需快递
     */
    private Integer deliveryType;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 总值
     */
    private Long total;

    /**
     * 订单状态 1:待付款 2:待发货 3:待收货(已发货) 5:成功 6:失败
     */
    private Integer status;

    /**
     * 订单商品总数
     */
    private Integer allCount;

    /**
     * 付款时间
     */
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 完成时间
     */
    private LocalDateTime finallyTime;

    /**
     * 结算时间
     */
    private LocalDateTime settledTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 是否已支付，1.已支付0.未支付
     */
    private Integer isPayed;

    /**
     * 订单关闭原因 1-超时未支付 4-买家取消 15-已通过货到付款交易
     */
    private Integer closeType;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    private Integer deleteStatus;

    /**
     * 订单版本号，每处理一次订单，版本号+1
     */
    private Integer version;

    /**
     * 用户订单地址id
     */
    private Long orderAddrId;
}