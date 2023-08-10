package com.xxw.shop;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Codegen {

    public static void main(String[] args) {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://10.66.70.183:3306/xxw-shop?useUnicode=true&characterEncoding=utf-8&useSSL" +
                "=false&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("xxw-shop");
        dataSource.setPassword("xxw-shop");

        //创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = createGlobalConfigUseStyle1();
        //GlobalConfig globalConfig = createGlobalConfigUseStyle2();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfigUseStyle1() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.setBasePackage("com.xxw.shop");

        //设置表前缀和只生成哪些表
//        globalConfig.setGenerateSchema("schema");
//        globalConfig.setTablePrefix("tb_");
//        globalConfig.setGenerateTable("menu","menu_permission","role","role_menu","user_role");
        globalConfig.setGenerateTable("order_info", "order_addr", "order_item");
        //设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);

        //设置生成 mapper
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.setServiceGenerateEnable(true);
        globalConfig.setServiceImplGenerateEnable(true);
        globalConfig.setControllerGenerateEnable(true);
        globalConfig.setTableDefGenerateEnable(true);
        globalConfig.setMapperXmlGenerateEnable(true);
        globalConfig.setPackageInfoGenerateEnable(true);

        //可以单独配置某个列
//        ColumnConfig columnConfig = new ColumnConfig();
//        columnConfig.setColumnName("tenant_id");
//        columnConfig.setLarge(true);
//        columnConfig.setVersion(true);
//        globalConfig.setColumnConfig("account", columnConfig);

        return globalConfig;
    }

    public static GlobalConfig createGlobalConfigUseStyle2() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.getPackageConfig().setBasePackage("com.test");

        //设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig().setGenerateSchema("schema").setTablePrefix("tb_").setGenerateTable("account"
                , "account_session");

        //设置生成 entity 并启用 Lombok
        globalConfig.enableEntity().setWithLombok(true);

        //设置生成 mapper
        globalConfig.enableMapper();

        //可以单独配置某个列
        ColumnConfig columnConfig = new ColumnConfig();
        columnConfig.setColumnName("tenant_id");
        columnConfig.setLarge(true);
        columnConfig.setVersion(true);
        globalConfig.getStrategyConfig().setColumnConfig("account", columnConfig);

        return globalConfig;
    }
}
