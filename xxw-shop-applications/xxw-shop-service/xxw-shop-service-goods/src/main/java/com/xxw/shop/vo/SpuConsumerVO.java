package com.xxw.shop.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xxw.shop.api.goods.vo.SpuAttrValueVO;
import com.xxw.shop.api.support.serializer.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SpuConsumerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "spu id")
    private Long spuId;

    @Schema(description = "店铺id")
    private Long shopId;

    @Schema(description = "spu名称")
    private String name;

    @Schema(description = "卖点")
    private String sellingPoint;

    @Schema(description = "商品介绍主图")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String mainImgUrl;

    @Schema(description = "商品介绍主图 多个图片逗号分隔")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String imgUrls;

    @Schema(description = "售价，整数方式保存")
    private Long priceFee;

    @Schema(description = "市场价，整数方式保存")
    private Long marketPriceFee;

    @Schema(description = "商品详情")
    private String detail;

    @Schema(description = "总库存")
    private Integer totalStock;

    @Schema(description = "规格属性")
    private List<SpuAttrValueVO> spuAttrValues;

    @Schema(description = "sku列表")
    private List<SkuConsumerVO> skus;

    @Schema(description = "商品销量")
    private Integer saleNum;
}
