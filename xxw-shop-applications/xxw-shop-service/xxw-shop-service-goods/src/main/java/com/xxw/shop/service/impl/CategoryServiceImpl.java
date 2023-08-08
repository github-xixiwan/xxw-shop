package com.xxw.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xxw.shop.cache.GoodsCacheNames;
import com.xxw.shop.constant.CategoryLevel;
import com.xxw.shop.constant.GoodsBusinessError;
import com.xxw.shop.entity.Category;
import com.xxw.shop.entity.table.CategoryTableDef;
import com.xxw.shop.mapper.CategoryMapper;
import com.xxw.shop.module.common.constant.Constant;
import com.xxw.shop.module.common.exception.BusinessException;
import com.xxw.shop.module.security.AuthUserContext;
import com.xxw.shop.service.CategoryService;
import com.xxw.shop.vo.CategoryVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务层实现。
 *
 * @author liaoxiting
 * @since 2023-08-08
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private CategoryVO getById(Long categoryId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(CategoryTableDef.CATEGORY.CATEGORY_ID.eq(categoryId));
        return this.getOneAs(queryWrapper, CategoryVO.class);
    }

    @Override
    public CategoryVO getCategoryId(Long categoryId) {
        CategoryVO category = getById(categoryId);
        if (Objects.isNull(category)) {
            return null;
        }
        List<CategoryVO> paths = new ArrayList<>();
        paths.add(category);
        getPathNames(paths);
        return category;
    }

    @Override
    public void saveCategory(Category category) {
        existCategoryName(category);
        category.setShopId(AuthUserContext.get().getTenantId());
        String path = "";
        if (!Objects.equals(CategoryLevel.First.value(), category.getLevel())) {
            String parentId = String.valueOf(category.getParentId());
            CategoryVO categoryDb = getById(category.getParentId());
            if (StrUtil.isBlank(categoryDb.getPath())) {
                path = parentId;
            } else {
                path = categoryDb.getPath() + Constant.CATEGORY_INTERVAL + parentId;
            }
        }
        category.setPath(path);
        this.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        CategoryVO dbCategory = getById(category.getCategoryId());
        if (Objects.equals(dbCategory.getCategoryId(), category.getParentId())) {
            throw new BusinessException(GoodsBusinessError.GOODS_00004);
        }
        existCategoryName(category);
        this.updateById(category);
    }

    @Override
    public void deleteById(Long categoryId) {
        int count = mapper.getCategoryUseNum(categoryId);
        if (count > 0) {
            throw new BusinessException(GoodsBusinessError.GOODS_00005);
        }
        this.removeById(categoryId);
    }

    @Override
    public List<CategoryVO> list(Long shopId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(CategoryTableDef.CATEGORY.SHOP_ID.eq(shopId));
        queryWrapper.and(CategoryTableDef.CATEGORY.STATUS.ne(-1));
        return this.listAs(queryWrapper, CategoryVO.class);
    }

    @Override
    public List<Long> listCategoryId(Long shopId, Long parentId) {
        return mapper.listCategoryId(shopId, parentId);
    }

    @Override
    @Caching(evict = {@CacheEvict(cacheNames = GoodsCacheNames.CATEGORY_LIST_BY_PARENT_ID_AND_SHOP_ID, key = "#shopId" +
            " + ':' + " + "#parentId"), @CacheEvict(cacheNames = GoodsCacheNames.CATEGORY_LIST_ALL_OF_SHOP, key =
            "#shopId"), @CacheEvict(cacheNames = GoodsCacheNames.CATEGORY_LIST, key = "#shopId + ':' + #parentId")})
    public void removeCategoryCache(Long shopId, Long parentId) {
    }

    @Override
    public void getPathNames(List<CategoryVO> categorys) {
        if (CollUtil.isEmpty(categorys)) {
            return;
        }
        // 获取分类的所有上级分类id集合
        Set<Long> paths = new HashSet<>();
        for (CategoryVO category : categorys) {
            if (Objects.isNull(category) || StrUtil.isBlank(category.getPath())) {
                continue;
            }
            String[] parentIds = category.getPath().split(Constant.CATEGORY_INTERVAL);
            for (String parentId : parentIds) {
                paths.add(Long.valueOf(parentId));
            }
        }
        if (CollUtil.isEmpty(paths)) {
            return;
        }

        // 获取所有上级分类id列表
        List<Category> listByCategoryIds = this.listByIds(paths);
        Map<Long, Category> categoryMap = listByCategoryIds.stream().collect(Collectors.toMap(Category::getCategoryId
                , c -> c));

        // 获取每个分类的上级分类名称集合
        for (CategoryVO category : categorys) {
            if (StrUtil.isBlank(category.getPath())) {
                continue;
            }
            String[] parentIdArray = category.getPath().split(Constant.CATEGORY_INTERVAL);
            category.setPathNames(new ArrayList<>());
            for (int i = 0; i < parentIdArray.length; i++) {
                String pathName = categoryMap.get(Long.valueOf(parentIdArray[i])).getName();
                category.getPathNames().add(pathName);
            }
        }
    }

    @Override
    public CategoryVO getPathNameByCategoryId(Long categoryId) {
        CategoryVO category = getById(categoryId);
        if (Objects.isNull(category)) {
            return null;
        }
        List<CategoryVO> categorys = new ArrayList<>(1);
        categorys.add(category);
        getPathNames(categorys);
        return category;
    }

    private List<CategoryVO> listByCondition(Long shopId, Long parentId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(CategoryTableDef.CATEGORY.SHOP_ID.eq(shopId));
        queryWrapper.and(CategoryTableDef.CATEGORY.PARENT_ID.eq(parentId));
        queryWrapper.and(CategoryTableDef.CATEGORY.STATUS.eq(1));
        return this.listAs(queryWrapper, CategoryVO.class);
    }

    @Override
    @Cacheable(cacheNames = GoodsCacheNames.CATEGORY_LIST, key = "#shopId + ':' + #parentId")
    public List<CategoryVO> categoryList(Long shopId, Long parentId) {
        if (!Objects.equals(shopId, Constant.PLATFORM_SHOP_ID) || Objects.equals(parentId, Constant.CATEGORY_ID)) {
            return listByCondition(shopId, parentId);
        }
        List<CategoryVO> categoryVOList = listByCondition(shopId, parentId);
        List<CategoryVO> categoryList =
                categoryVOList.stream().filter(c -> c.getParentId().equals(parentId)).collect(Collectors.toList());
        setChildCategory(categoryList, categoryVOList);
        return categoryList;
    }

    @Override
    @Cacheable(cacheNames = GoodsCacheNames.CATEGORY_LIST_BY_PARENT_ID_AND_SHOP_ID, key = "#shopId + ':' + #parentId")
    public List<CategoryVO> listByShopIdAndParenId(Long shopId, Long parentId) {
        return listByCondition(shopId, parentId);
    }

    @Override
    @Cacheable(cacheNames = GoodsCacheNames.CATEGORY_LIST_ALL_OF_SHOP, key = "#shopId")
    public List<CategoryVO> shopCategoryList(Long shopId) {
        List<CategoryVO> list = listByCondition(shopId, null);
        Map<Integer, List<CategoryVO>> categoryMap = list.stream().collect(Collectors.groupingBy(CategoryVO::getLevel));

        List<CategoryVO> secondCategories = categoryMap.get(CategoryLevel.SECOND.value());
        if (Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            // 三级分类
            List<CategoryVO> thirdCategories = categoryMap.get(CategoryLevel.THIRD.value());
            //二级分类
            setChildCategory(secondCategories, thirdCategories);
        }
        //一级分类
        List<CategoryVO> firstCategories = categoryMap.get(CategoryLevel.First.value());
        setChildCategory(firstCategories, secondCategories);
        return firstCategories;
    }

    @Override
    public List<Category> getChildCategory(Long categoryId) {
        return mapper.getChildCategory(categoryId);
    }

    @Override
    public void updateBatchOfStatus(List<Long> updateList, Integer status) {
        if (CollUtil.isEmpty(updateList) || Objects.isNull(status)) {
            return;
        }
        mapper.updateBatchOfStatus(updateList, status);
    }

    private void setChildCategory(List<CategoryVO> categories, List<CategoryVO> childCategories) {
        if (CollUtil.isEmpty(categories) || CollUtil.isEmpty(childCategories)) {
            return;
        }
        Map<Long, List<CategoryVO>> secondCategoryMap =
                childCategories.stream().collect(Collectors.groupingBy(CategoryVO::getParentId));
        for (CategoryVO category : categories) {
            category.setCategories(secondCategoryMap.get(category.getCategoryId()));
        }
    }

    /**
     * 校验分类名是否已存在
     *
     * @param category
     */
    private void existCategoryName(Category category) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(CategoryTableDef.CATEGORY.NAME.eq(category.getName()));
        queryWrapper.and(CategoryTableDef.CATEGORY.PARENT_ID.eq(category.getParentId()));
        queryWrapper.and(CategoryTableDef.CATEGORY.CATEGORY_ID.eq(category.getCategoryId()));
        queryWrapper.and(CategoryTableDef.CATEGORY.SHOP_ID.eq(AuthUserContext.get().getTenantId()));
        long countByName = this.count(queryWrapper);
        if (countByName > 0) {
            throw new BusinessException(GoodsBusinessError.GOODS_00003);
        }
    }
}
