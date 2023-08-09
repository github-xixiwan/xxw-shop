package com.xxw.shop.api.goods.feign;

import com.xxw.shop.api.goods.vo.EsGoodsVO;
import com.xxw.shop.module.common.response.ServerResponseEntity;
import com.xxw.shop.module.web.feign.FeignInsideAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "shop-goods", contextId = "productFeign")
public interface ProductFeignClient {

    /**
     * 通过spuId需要搜索的商品
     *
     * @param spuId spuid
     * @return es保存的商品信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/loadEsProductBO")
    ServerResponseEntity<EsGoodsVO> loadEsGoodsVO(@RequestParam("spuId") Long spuId);

    /**
     * 根据平台categoryId，获取spuId列表
     *
     * @param shopCategoryIds
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByShopCategoryIds")
    ServerResponseEntity<List<Long>> getSpuIdsByShopCategoryIds(@RequestParam("shopCategoryIds") List<Long> shopCategoryIds);

    /**
     * 根据categoryId列表，获取spuId列表
     *
     * @param categoryIds
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByCategoryIds")
    ServerResponseEntity<List<Long>> getSpuIdsByCategoryIds(@RequestParam("categoryIds") List<Long> categoryIds);

    /**
     * 根据brandId，获取spuId列表
     *
     * @param brandId
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByBrandId")
    ServerResponseEntity<List<Long>> getSpuIdsByBrandId(@RequestParam("brandId") Long brandId);

    /**
     * 根据店铺id，获取spuId列表
     *
     * @param shopId
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByShopId")
    ServerResponseEntity<List<Long>> getSpuIdsByShopId(@RequestParam("shopId") Long shopId);

}
