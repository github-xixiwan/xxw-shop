package com.xxw.shop.vo;

import com.xxw.shop.module.web.vo.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderAddrVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long orderAddrId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "收货人")
    private String consignee;

    @Schema(description = "省ID")
    private Long provinceId;

    @Schema(description = "省")
    private String province;

    @Schema(description = "城市ID")
    private Long cityId;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "区")
    private String area;

    @Schema(description = "地址")
    private String addr;

    @Schema(description = "邮编")
    private String postCode;

    @Schema(description = "手机")
    private String mobile;
}
