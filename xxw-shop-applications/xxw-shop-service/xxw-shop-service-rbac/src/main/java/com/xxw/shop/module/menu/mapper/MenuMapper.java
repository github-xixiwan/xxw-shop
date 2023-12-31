package com.xxw.shop.module.menu.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xxw.shop.module.menu.entity.Menu;
import com.xxw.shop.module.menu.vo.MenuSimpleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  映射层。
 *
 * @author liaoxiting
 * @since 2023-07-31
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据菜单管理id删除菜单管理
     *
     * @param menuId
     * @param sysType
     */
    void removeById(@Param("menuId") Long menuId, @Param("sysType") Integer sysType);

    /**
     * 根据系统类型获取该系统的菜单列表
     *
     * @param sysType 系统类型
     * @return 菜单列表
     */
    List<Menu> listBySysType(@Param("sysType") Integer sysType);

    /**
     * 根据系统类型获取该系统的菜单列表，只有id和名字
     *
     * @param sysType sysType 系统类型
     * @return 简易菜单信息列表
     */
    List<MenuSimpleVO> listSimpleMenuBySytType(Integer sysType);

    /**
     * 根据系统类型获取该系统的菜单列表 + 菜单下的权限列表
     *
     * @param sysType 系统类型
     * @return 菜单列表 + 菜单下的权限列表
     */
    List<MenuSimpleVO> listWithPermissions(Integer sysType);

    /**
     * 获取当前用户可见的菜单ids
     *
     * @param userId  用户id
     * @return 菜单列表
     */
    List<Long> listMenuIds(@Param("userId") Long userId);
}
