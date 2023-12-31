package com.xxw.shop.module.menu.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xxw.shop.cache.RbacCacheNames;
import com.xxw.shop.module.menu.entity.Menu;
import com.xxw.shop.module.menu.entity.table.MenuTableDef;
import com.xxw.shop.module.menu.mapper.MenuMapper;
import com.xxw.shop.module.menu.service.MenuService;
import com.xxw.shop.module.menu.vo.MenuSimpleVO;
import com.xxw.shop.module.menu.vo.MenuVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务层实现。
 *
 * @author liaoxiting
 * @since 2023-07-31
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public MenuVO getByMenuId(Long menuId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(MenuTableDef.MENU.MENU_ID.eq(menuId));
        return this.getOneAs(queryWrapper, MenuVO.class);
    }

    @Override
    @CacheEvict(cacheNames = RbacCacheNames.MENU_LIST_KEY, key = "#menu.bizType")
    public boolean saveMenu(Menu menu) {
        this.save(menu);
        return true;
    }

    @Override
    @CacheEvict(cacheNames = RbacCacheNames.MENU_LIST_KEY, key = "#menu.bizType")
    public void modify(Menu menu) {
        this.updateById(menu);
    }

    @Override
    @CacheEvict(cacheNames = RbacCacheNames.MENU_LIST_KEY, key = "#sysType")
    public void removeById(Long menuId, Integer sysType) {
        mapper.removeById(menuId, sysType);
    }

    @Override
    @Cacheable(cacheNames = RbacCacheNames.MENU_LIST_KEY, key = "#sysType")
    public List<Menu> listBySysType(Integer sysType) {
        return mapper.listBySysType(sysType);
    }

    @Override
    public List<MenuSimpleVO> listWithPermissions(Integer sysType) {
        return mapper.listWithPermissions(sysType);
    }

    @Override
    @Cacheable(cacheNames = RbacCacheNames.MENU_ID_LIST_KEY, key = "#userId")
    public List<Long> listMenuIds(Long userId) {
        return mapper.listMenuIds(userId);
    }
}
