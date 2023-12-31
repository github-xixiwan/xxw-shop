package com.xxw.shop.constant;

/**
 * es当中的index
 */
public enum EsIndexEnum {

    /**
     * 商品
     */
    GOODS("goods"),

    /**
     * 订单
     */
    ORDER("order"),

    ;

    private final String value;

    public String value() {
        return value;
    }

    EsIndexEnum(String value) {
        this.value = value;
    }
}
