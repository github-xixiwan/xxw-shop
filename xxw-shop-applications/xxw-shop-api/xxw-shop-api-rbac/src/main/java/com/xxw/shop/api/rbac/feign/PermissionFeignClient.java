package com.xxw.shop.api.rbac.feign;

import com.xxw.shop.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.xxw.shop.module.web.response.ServerResponseEntity;
import com.xxw.shop.module.web.security.Auth;
import com.xxw.shop.module.web.security.FeignInsideAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shop-rbac", contextId = "permission")
public interface PermissionFeignClient {

    /**
     * 校验是否有某个uri的权限
     *
     * @param userId
     * @param sysType
     * @param uri
     * @param isAdmin
     * @param method
     * @return 是否有某个uri的权限
     */
    @GetMapping(value = Auth.CHECK_RBAC_URI)
    ServerResponseEntity<Boolean> checkPermission(@RequestParam("userId") Long userId, @RequestParam("sysType") Integer sysType,
                                                  @RequestParam("uri") String uri, @RequestParam("isAdmin") Integer isAdmin,
                                                  @RequestParam("method") Integer method);

    /**
     * 清除用户权限缓存
     *
     * @param clearUserPermissionsCacheDTO
     * @return 是否调用成功
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/permission/clearUserPermissionsCache")
    ServerResponseEntity<Void> clearUserPermissionsCache(@RequestBody ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO);

}