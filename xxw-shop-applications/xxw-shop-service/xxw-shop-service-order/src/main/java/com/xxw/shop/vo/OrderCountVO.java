package com.xxw.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "我的订单数量")
public class OrderCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "所有订单数量")
    private Integer allCount;

    @Schema(description = "待付款")
    private Integer unPay;

    @Schema(description = "待发货")
    private Integer payed;

    @Schema(description = "待收货")
    private Integer consignment;

    @Schema(description = "已完成")
    private Integer success;
}
