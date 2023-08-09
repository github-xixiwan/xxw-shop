package com.xxw.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xxw.shop.cache.GoodsCacheNames;
import com.xxw.shop.constant.GoodsBusinessError;
import com.xxw.shop.dto.SpuDTO;
import com.xxw.shop.entity.*;
import com.xxw.shop.mapper.SpuMapper;
import com.xxw.shop.module.cache.tool.IGlobalRedisCache;
import com.xxw.shop.module.common.cache.CacheNames;
import com.xxw.shop.module.common.constant.Constant;
import com.xxw.shop.module.common.constant.StatusEnum;
import com.xxw.shop.module.common.exception.BusinessException;
import com.xxw.shop.module.common.page.PageDTO;
import com.xxw.shop.module.common.response.ServerResponseEntity;
import com.xxw.shop.module.security.AuthUserContext;
import com.xxw.shop.service.*;
import com.xxw.shop.vo.CategoryVO;
import com.xxw.shop.vo.SpuAttrValueVO;
import com.xxw.shop.vo.SpuVO;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import ma.glasnost.orika.MapperFacade;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.xxw.shop.entity.table.SpuSkuAttrValueTableDef.SPU_SKU_ATTR_VALUE;
import static com.xxw.shop.entity.table.SpuTableDef.SPU;

/**
 * 服务层实现。
 *
 * @author liaoxiting
 * @since 2023-08-08
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {

    @Resource
    private IGlobalRedisCache globalRedisCache;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private SpuExtensionService spuExtensionService;

    @Resource
    private SpuAttrValueService spuAttrValueService;

    @Resource
    private SpuDetailService spuDetailService;

    @Resource
    private SkuService skuService;

    @Override
    @Cacheable(cacheNames = GoodsCacheNames.SPU_KEY, key = "#spuId", sync = true)
    public SpuVO getBySpuId(Long spuId) {
        return mapper.getBySpuId(spuId);
    }

    @Override
    @Caching(evict = {@CacheEvict(cacheNames = GoodsCacheNames.SPU_KEY, key = "#spuId"), @CacheEvict(cacheNames =
            GoodsCacheNames.SPU_EXTENSION_KEY, key = "#spuId")})
    public void removeSpuCacheBySpuId(Long spuId) {
    }

    @Override
    public void batchRemoveSpuCacheBySpuId(List<Long> spuIds) {
        List<String> spuKeys = new ArrayList<>();
        List<String> spuExtensionKeys = new ArrayList<>();
        for (Long key : spuIds) {
            spuKeys.add(GoodsCacheNames.SPU_KEY + CacheNames.UNION + key);
            spuExtensionKeys.add(GoodsCacheNames.SPU_EXTENSION_KEY + CacheNames.UNION + key);
        }
        globalRedisCache.del(spuKeys.toArray(new String[0]));
        globalRedisCache.del(spuExtensionKeys.toArray(new String[0]));
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void changeSpuStatus(Long spuId, Integer status) {
        Spu spu = new Spu();
        spu.setSpuId(spuId);
        spu.setStatus(status);
        this.updateById(spu);
        if (!Objects.equals(status, StatusEnum.ENABLE.value())) {
            SpuVO spuVO = mapper.getBySpuId(spuId);
            ServerResponseEntity<Void> imgRes = indexImgFeignClient.deleteBySpuId(spuVO.getSpuId(), spuVO.getShopId());
            if (!imgRes.isSuccess()) {
                throw new Mall4cloudException("服务异常");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpuDTO spuDTO) {
        Spu spu = mapperFacade.map(spuDTO, Spu.class);
        spu.setShopId(AuthUserContext.get().getTenantId());
        spu.setStatus(StatusEnum.ENABLE.value());
        spu.setShopId(AuthUserContext.get().getTenantId());
        // 1.保存商品信息
        this.save(spu);
        // 2.保存商品其他信息，规格、详细、扩展信息
        List<SpuAttrValue> spuAttrValues = mapperFacade.mapAsList(spuDTO.getSpuAttrValues(), SpuAttrValue.class);
        for (SpuAttrValue spuAttrValue : spuAttrValues) {
            if (Objects.isNull(spuAttrValue.getAttrValueId())) {
                spuAttrValue.setAttrValueId(0L);
            }
        }
        spuAttrValueService.saveBatch(spu.getSpuId(), spuAttrValues);
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spu.getSpuId());
        spuDetail.setDetail(spuDTO.getDetail());
        spuDetailService.save(spuDetail);

        SpuExtension spuExtension = new SpuExtension();
        spuExtension.setSpuId(spu.getSpuId());
        spuExtension.setActualStock(spuDTO.getTotalStock());
        spuExtension.setStock(spuDTO.getTotalStock());
        spuExtensionService.save(spuExtension);

        // 3.保存sku信息
        skuService.saveSku(spu.getSpuId(), spuDTO.getSkuList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpu(SpuDTO spuDTO) {
        Spu spu = mapperFacade.map(spuDTO, Spu.class);
        SpuServiceImpl spuService = (SpuServiceImpl) AopContext.currentProxy();
        SpuVO dbSpu = spuService.getBySpuId(spu.getSpuId());
        // 1.修改商品信息
        this.updateById(spu);
        List<SpuAttrValue> spuAttrValues = mapperFacade.mapAsList(spuDTO.getSpuAttrValues(), SpuAttrValue.class);
        spuAttrValueService.updateSpuAttrValue(spu.getSpuId(), spuAttrValues, dbSpu.getSpuAttrValues());

        // 2.修改商品详情
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spu.getSpuId());
        spuDetail.setDetail(spuDTO.getDetail());
        spuDetailService.updateById(spuDetail);

        // 3.修改商品库存
        if (Objects.nonNull(spuDTO.getChangeStock()) && spuDTO.getChangeStock() > 0) {
            SpuExtension spuExtension = new SpuExtension();
            spuExtension.setSpuId(spu.getSpuId());
            spuExtension.setStock(spuDTO.getChangeStock());
            spuExtensionService.updateStock(spu.getSpuId(), spuDTO.getTotalStock());
        }

        // 4.修改商品sku信息
        skuService.updateSku(spu.getSpuId(), spuDTO.getSkuList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void deleteById(Long spuId) {
        SpuVO spuVO = getBySpuId(spuId);
        if (Objects.isNull(spuVO) || Objects.equals(spuVO.getStatus(), StatusEnum.DELETE.value())) {
            throw new BusinessException(GoodsBusinessError.GOODS_00006);
        }
        // 删除商品、sku信息(逻辑删除)
        Spu spu = new Spu();
        spu.setSpuId(spuId);
        spu.setStatus(-1);
        this.updateById(spu);

        skuService.deleteBySpuId(spuId);
        // 把轮播图中关联了该商品的数据删除
        ServerResponseEntity<Void> imgRes = indexImgFeignClient.deleteBySpuId(spuId, spuVO.getShopId());
        if (!imgRes.isSuccess()) {
            throw new Mall4cloudException("服务异常");
        }
    }

    @Override
    public void updateSpuOrSku(SpuDTO spuDTO) {
        Spu spu = new Spu();
        spu.setSpuId(spuDTO.getSpuId());
        spu.setName(spuDTO.getName());
        spu.setSeq(spuDTO.getSeq());
        if (CollUtil.isNotEmpty(spuDTO.getSkuList())) {
            skuService.updateAmountOrStock(spuDTO);
        }
        if (Objects.nonNull(spuDTO.getChangeStock()) && spuDTO.getChangeStock() > 0) {
            spuExtensionService.updateStock(spuDTO.getSpuId(), spuDTO.getChangeStock());
            return;
        }
        spu.setPriceFee(spuDTO.getPriceFee());
        this.updateById(spu);
    }

    @Override
    public void updateSpuUpdateTime(List<Long> spuIds, List<Long> categoryIds) {
        if (CollUtil.isEmpty(spuIds) && CollUtil.isEmpty(categoryIds)) {
            return;
        }
        mapper.updateSpuUpdateTime(spuIds, categoryIds);
    }

    @Override
    public EsProductBO loadEsProductBO(Long spuId) {
        // 获取商品、品牌数据
        EsProductBO esProductBO = spuMapper.loadEsProductBO(spuId);
        // 获取分类数据
        CategoryVO category = categoryService.getPathNameByCategoryId(esProductBO.getCategoryId());
        String[] categoryIdArray = category.getPath().split(Constant.CATEGORY_INTERVAL);
        esProductBO.setCategoryName(category.getName());
        for (int i = 0; i < categoryIdArray.length; i++) {
            if (i == 0) {
                esProductBO.setPrimaryCategoryId(Long.valueOf(categoryIdArray[i]));
                esProductBO.setPrimaryCategoryName(category.getPathNames().get(i));
            } else {
                esProductBO.setSecondaryCategoryId(Long.valueOf(categoryIdArray[i]));
                esProductBO.setSecondaryCategoryName(category.getPathNames().get(i));
            }
        }
        CategoryVO shopCategory = categoryService.getById(esProductBO.getShopSecondaryCategoryId());
        if (Objects.nonNull(shopCategory)) {
            esProductBO.setShopSecondaryCategoryName(shopCategory.getName());
            esProductBO.setShopPrimaryCategoryId(shopCategory.getParentId());
//            esProductBO.setShopPrimaryCategoryName(shopCategory.getPathNames().get(0));
        }
        // 获取属性
        List<SpuAttrValueVO> spuAttrsBySpuId = spuAttrValueService.getSpuAttrsBySpuId(spuId);
        List<SpuAttrValueVO> attrs =
                spuAttrsBySpuId.stream().filter(spuAttrValueVO -> spuAttrValueVO.getSearchType().equals(1)).collect(Collectors.toList());
        esProductBO.setAttrs(mapperFacade.mapAsList(attrs, EsAttrBO.class));
        return esProductBO;
    }

    @Override
    public List<Long> getSpuIdsBySpuUpdateDTO(List<Long> shopCategoryIds, List<Long> categoryIds, Long brandId,
                                              Long shopId) {
        if (CollUtil.isEmpty(shopCategoryIds) && CollUtil.isEmpty(categoryIds) && Objects.isNull(brandId) && Objects.isNull(shopId)) {
            return new ArrayList<>();
        }
        return spuMapper.getSpuIdsBySpuUpdateDTO(shopCategoryIds, categoryIds, brandId, shopId);
    }

    @Override
    public List<SpuVO> listBySpuIds(List<Long> spuIds, String prodName, Integer isFailure) {
        if (CollUtil.isEmpty(spuIds)) {
            return new ArrayList<>();
        }
        return spuMapper.listBySpuIds(spuIds, prodName, isFailure);
    }

    @Override
    public PageVO<SpuVO> platformPage(PageDTO pageDTO, SpuPageSearchDTO spuDTO) {
        PageVO<SpuVO> page = page(pageDTO, spuDTO);
        Set<Long> shopIdSet = page.getList().stream().map(SpuVO::getShopId).collect(Collectors.toSet());
        ServerResponseEntity<List<ShopDetailVO>> shopResponse =
                shopDetailFeignClient.listByShopIds(new ArrayList<>(shopIdSet));
        Map<Long, ShopDetailVO> shopMap =
                shopResponse.getData().stream().collect(Collectors.toMap(ShopDetailVO::getShopId, s -> s));
        for (SpuVO spuVO : page.getList()) {
            ShopDetailVO shopDetailVO = shopMap.get(spuVO.getShopId());
            if (Objects.isNull(shopDetailVO)) {
                continue;
            }
            spuVO.setShopName(shopDetailVO.getShopName());
        }
        return page;
    }

    @Override
    public PageVO<SpuVO> pageByLangAndTagId(PageDTO pageDTO, SpuDTO spuDTO, Integer isContain) {
        return PageUtil.doPage(pageDTO, () -> spuMapper.pageByLangAndTagId(pageDTO, spuDTO, isContain));
    }

    @Override
    public void batchChangeSpuStatusByCids(List<Long> cidList, Long shopId, Integer status) {
        List<Long> spuIdList;
        if (Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            spuIdList = spuMapper.listIdsByCidListAndTypeAndShopId(cidList, 1, shopId);
        } else {
            spuIdList = spuMapper.listIdsByCidListAndTypeAndShopId(cidList, 2, shopId);
        }
        if (Objects.isNull(spuIdList) || spuIdList.size() == 0) {
            return;
        }
        spuMapper.batchChangeSpuStatusBySpuIdsAndStatus(spuIdList, status);
        this.batchRemoveSpuCacheBySpuId(spuIdList);
    }

    @Override
    public long getUseNum(Long brandId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(SPU.BRAND_ID.eq(brandId));
        return this.count(queryWrapper);
    }
}