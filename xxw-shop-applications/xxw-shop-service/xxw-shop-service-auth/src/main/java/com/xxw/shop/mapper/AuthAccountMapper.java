package com.xxw.shop.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xxw.shop.entity.AuthAccount;
import com.xxw.shop.module.common.bo.AuthAccountInVerifyBO;
import com.xxw.shop.api.auth.vo.AuthAccountVO;
import org.apache.ibatis.annotations.Param;

/**
 *  映射层。
 *
 * @author liaoxiting
 * @since 2023-08-01
 */
public interface AuthAccountMapper extends BaseMapper<AuthAccount> {

    /**
     * 根据输入的用户名及用户名类型获取用户信息
     *
     * @param inputUserNameType 输入的用户名类型 1.username 2.mobile 3.email
     * @param inputUserName     输入的用户名
     * @param sysType           系统类型
     * @return 用户在token中信息 + 数据库中的密码
     */
    AuthAccountInVerifyBO getAuthAccountInVerifyByInputUserName(@Param("inputUserNameType") Integer inputUserNameType,
                                                                @Param("inputUserName") String inputUserName, @Param("sysType") Integer sysType);

    /**
     * 根据用户id 和系统类型获取平台唯一用户
     *
     * @param userId  用户id
     * @param sysType 系统类型
     * @return 平台唯一用户
     */
    AuthAccount getByUserIdAndType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

    /**
     * 更新密码 根据用户id 和系统类型
     *
     * @param userId      用户id
     * @param sysType     系统类型
     * @param newPassWord 新密码
     */
    void modifyPassword(@Param("userId") Long userId, @Param("sysType") Integer sysType, @Param("newPassWord") String newPassWord);

    /**
     * 更新
     *
     * @param authAccount authAccount
     */
    void modifyAccountInfo(@Param("authAccount") AuthAccount authAccount);

    /**
     * 根据用户id和系统类型删除用户
     *
     * @param userId  用户id
     * @param sysType 系统类型
     */
    void deleteByUserIdAndSysType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

    /**
     * 根据用户名和系统类型获取用户信息
     * @param validAccount
     * @param systemType
     * @return uid
     */
    AuthAccount getAccountByInputUserName(@Param("validAccount") String validAccount, @Param("systemType") Integer systemType);

    /**
     * 根据用户名和系统类型获取用户信息
     * @param username
     * @param sysType
     * @return
     */
    AuthAccountVO getByUsernameAndSysType(@Param("userName") String username, @Param("sysType") Integer sysType);

    /**
     * 根据用户id更新租户id
     * @param authAccount
     * @param userId
     * @param sysType
     * @return
     */
    int modifyUserInfoByUserId(@Param("authAccount") AuthAccount authAccount, @Param("userId") Long userId, @Param(
            "sysType") Integer sysType);

    /**
     * 根据租户id获取商家信息
     * @param tenantId
     * @return
     */
    AuthAccountVO getMerchantInfoByTenantId(@Param("tenantId") Long tenantId);

}
