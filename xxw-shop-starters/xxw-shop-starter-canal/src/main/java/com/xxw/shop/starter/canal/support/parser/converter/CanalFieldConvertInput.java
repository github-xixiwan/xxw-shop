package com.xxw.shop.starter.canal.support.parser.converter;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.sql.SQLType;

@SuppressWarnings("rawtypes")
@Builder
@Data
public class CanalFieldConvertInput {

    private Class<?> fieldKlass;
    private Class<? extends BaseCanalFieldConverter> converterKlass;
    private SQLType sqlType;

    @Tolerate
    public CanalFieldConvertInput() {

    }
}
