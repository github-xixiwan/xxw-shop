package com.xxw.shop.constant;

/**
 * 搜索平台的类型
 * @author YXF
 * @date 2021/03/16
 */
public enum SearchTypeEnum {

    /**
     * 用户端搜索
     */
    CONSUMER(1),

    /**
     * 商家端、平台端搜索
     */
    BUSINESS(2),

    /**
     * 商家端、平台端搜索
     */
    PLATFORM(3)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    SearchTypeEnum(Integer value) {
        this.value = value;
    }
}
