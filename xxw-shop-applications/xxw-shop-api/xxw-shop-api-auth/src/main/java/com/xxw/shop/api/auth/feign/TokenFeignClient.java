package com.xxw.shop.api.auth.feign;

import com.xxw.shop.module.common.bo.UserInfoInTokenBO;
import com.xxw.shop.module.common.response.ServerResponseEntity;
import com.xxw.shop.module.web.constant.Auth;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shop-auth", contextId = "tokenFeign")
public interface TokenFeignClient {

    /**
     * 校验token并返回token保存的用户信息
     *
     * @param accessToken accessToken
     * @return token保存的用户信息
     */
    @GetMapping(value = Auth.CHECK_TOKEN_URI)
    ServerResponseEntity<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);

}
