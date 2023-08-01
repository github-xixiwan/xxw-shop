package com.xxw.shop.constant;

import com.xxw.shop.module.util.exception.ErrorEnumInterface;

/**
 * RBAC业务级抛错
 */
public enum RbacBusinessError implements ErrorEnumInterface {

    RBAC_00001("RBAC_00001", "权限编码已存在，请勿重复添加");

    private String code;

    private String msg;

    RbacBusinessError(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
