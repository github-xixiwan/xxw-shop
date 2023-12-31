package com.xxw.shop.starter.canal.support.parser.converter;

import java.sql.JDBCType;

public class IntCanalFieldConverter extends BaseCanalFieldConverter<Integer> {

    /**
     * 单例
     */
    public static final BaseCanalFieldConverter<Integer> X = new IntCanalFieldConverter();

    private IntCanalFieldConverter() {
        super(JDBCType.INTEGER, Integer.class);
    }

    @Override
    protected Integer convertInternal(String source) {
        return Integer.valueOf(source);
    }
}
