package com.xxw.shop.module.common.response;

import com.xxw.shop.module.common.constant.SystemErrorEnumError;
import com.xxw.shop.module.common.exception.ErrorEnumInterface;

import java.io.Serializable;
import java.util.Objects;

public class ServerResponseEntity<T> implements Serializable {

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return Objects.equals(SystemErrorEnumError.OK.getCode(), this.code);
    }

    @Override
    public String toString() {
        return "ServerResponseEntity{" + "code=" + code + ", message='" + message + '\'' + ", data=" + data + '}';
    }

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(SystemErrorEnumError.OK.getCode());
        serverResponseEntity.setMessage(SystemErrorEnumError.OK.getMessage());
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(SystemErrorEnumError.OK.getCode());
        serverResponseEntity.setMessage(SystemErrorEnumError.OK.getMessage());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String message) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(SystemErrorEnumError.SHOW_FAIL.getCode());
        serverResponseEntity.setMessage(message);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ErrorEnumInterface errorEnum) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(errorEnum.getCode());
        serverResponseEntity.setMessage(errorEnum.getMessage());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ErrorEnumInterface errorEnum, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(errorEnum.getCode());
        serverResponseEntity.setMessage(errorEnum.getMessage());
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> transform(ServerResponseEntity<?> oldServerResponseEntity) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(SystemErrorEnumError.SHOW_FAIL.getCode());
        serverResponseEntity.setMessage(oldServerResponseEntity.getMessage());
        return serverResponseEntity;
    }

}
