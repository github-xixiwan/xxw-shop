package com.xxw.shop.bo;

import com.xxw.shop.starter.canal.annotation.CanalModel;
import com.xxw.shop.starter.canal.common.FieldNamingPolicy;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌信息
 */
@Data
@CanalModel(database = "xxw-shop", table = "brand", fieldNamingPolicy = FieldNamingPolicy.LOWER_UNDERSCORE)
public class BrandBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * brand_id
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌描述
     */
    private String desc;

    /**
     * 品牌logo图片
     */
    private String imgUrl;
}
