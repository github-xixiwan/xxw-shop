package com.xxw.shop.api.business.feign;

import com.xxw.shop.api.business.vo.ShopDetailVO;
import com.xxw.shop.module.common.response.ServerResponseEntity;
import com.xxw.shop.module.web.feign.FeignInsideAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "shop-business", contextId = "shopDetailFeign")
public interface ShopDetailFeignClient {

    /**
     * 根据店铺id获取店铺名称
     *
     * @param shopId 店铺id
     * @return 店铺名称
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopNameByShopId")
    ServerResponseEntity<String> getShopNameByShopId(@RequestParam("shopId") Long shopId);

    /**
     * 根据店铺id获取店铺信息
     *
     * @param shopId 店铺id
     * @return 店铺信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopByShopId")
    ServerResponseEntity<ShopDetailVO> getShopByShopId(@RequestParam("shopId") Long shopId);

    /**
     * 根据店铺id列表， 获取店铺列表信息
     *
     * @param shopIds 店铺id列表
     * @return 店铺列表信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/listByShopIds")
    ServerResponseEntity<List<ShopDetailVO>> listByShopIds(@RequestParam("shopIds") List<Long> shopIds);

    /**
     * 获取店铺信息及扩展信息
     *
     * @param shopId 店铺id
     * @return 店铺信息及扩展信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopExtension")
    ServerResponseEntity<ShopDetailVO> shopExtensionData(@RequestParam("shopId") Long shopId);

    /**
     * 获取店铺信息及扩展信息
     *
     * @param shopIds  店铺ids
     * @param shopName 店铺名称
     * @return 店铺信息列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail" +
            "/getShopDetailByShopIdAndShopName")
    ServerResponseEntity<List<ShopDetailVO>> getShopDetailByShopIdAndShopName(@RequestParam("shopIds") List<Long> shopIds,
                                                                              @RequestParam(value = "shopName",
                                                                                      defaultValue = "") String shopName);
}
