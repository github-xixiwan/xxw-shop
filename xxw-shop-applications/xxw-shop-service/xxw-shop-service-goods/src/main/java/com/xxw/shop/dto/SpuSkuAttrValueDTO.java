package com.xxw.shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpuSkuAttrValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商品sku销售属性关联信息id")
    private Integer spuSkuAttrId;

    @Schema(description = "SPU ID")
    private Long spuId;

    @Schema(description = "SKU ID")
    private Long skuId;

    @Schema(description = "销售属性ID")
    private Long attrId;

    @Schema(description = "销售属性名称")
    private String attrName;

    @Schema(description = "销售属性值ID")
    private Long attrValueId;

    @Schema(description = "销售属性值")
    private String attrValueName;

    @Schema(description = "状态 1:enable, 0:disable")
    private Integer status;
}
