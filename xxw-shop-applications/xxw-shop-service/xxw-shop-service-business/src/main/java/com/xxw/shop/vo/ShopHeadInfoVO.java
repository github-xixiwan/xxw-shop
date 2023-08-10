/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.xxw.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShopHeadInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description = "店铺id")
    private Long shopId;

    @Schema(description = "店铺类型1自营店 2普通店")
    private Integer type;

    @Schema(description = "店长用户id")
    private Long ownerUserId;

    @Schema(description = "店铺名称")
    private String shopName;

    @Schema(description = "店铺简介")
    private String intro;

    @Schema(description = "手机号码")
    private String noticeMobile;

    @Schema(description = "店铺logo(可修改)")
    private String shopLogo;

    @Schema(description = "是否优选好店 1.是 0.不是")
    private Integer isPreferred;

    @Schema(description = "店铺状态(-1:未开通 0: 停业中 1:营业中 2:平台下线 3:平台下线待审核)")
    private Integer shopStatus;

    @Schema(description = "移动端背景图")
    private String mobileBackgroundPic;

    @Schema(description = "pc背景图")
    private String pcBackgroundPic;
}
