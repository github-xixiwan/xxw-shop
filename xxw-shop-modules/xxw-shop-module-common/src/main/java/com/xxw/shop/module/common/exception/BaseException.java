package com.xxw.shop.module.common.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 异常基础类
 *
 * @author xxw
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private String errorName;
    private String code;

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BaseException() {
    }

    public BaseException(ErrorEnumInterface errorEnum, String message) {
        super(message);
        this.errorName = errorEnum.toString();
        this.message = message;
        if (errorEnum.getCode() != null) {
            this.code = errorEnum.getCode();
        } else {
            this.code = "A00001";
        }

    }

    public BaseException(String code, String message) {
        super(message);
        this.errorName = message;
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        if (errorName != null) {
            return errorName + ":" + code + ":" + message;
        } else {
            return super.toString();
        }
    }

    static String getErrorMessage(String message, Object... args) {
        if (args != null && args.length > 0) {
            if (!StrUtil.isBlank(message)) {
                message = String.format(message, args);
            } else {
                message = "SG 'Sprintf' error, 无法找到错误的 message，请检查错误枚举类 getMessage 的实现是否正确";
            }
        }

        return message;
    }
}