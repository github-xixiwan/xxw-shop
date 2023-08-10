package com.xxw.shop.constant;

/**
 * 配送类型
 */
public enum DeliveryType {


    /**
     * 无需快递
     */
    NOT_DELIVERY(3);

    private final Integer num;

    public Integer value() {
        return num;
    }

    DeliveryType(Integer num) {
        this.num = num;
    }

    public static DeliveryType instance(Integer value) {
        DeliveryType[] enums = values();
        for (DeliveryType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
