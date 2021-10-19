package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SkuMapper;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuValuesResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.AttributeException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sku表	 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    @Autowired
    private SpuService spuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttributeValuesService attributeValuesService;
    @Transactional
    @Override
    public void add(SkuParam param){
        List<Long> ids = new ArrayList<>();

        StringBuffer stringBuffer = new StringBuffer();
        for (SkuValues skuValue : param.getSkuValues()) {
            ids.add(skuValue.getAttributeValuesId());
        }
        Collections.sort(ids);
        for (Long id : ids) {
            stringBuffer.append(id+",");
        }
        if (stringBuffer.length()>1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        Sku entity = getEntity(param);
        entity.setSkuName(stringBuffer.toString());
        this.save(entity);
    }

    @Override
    public void delete(SkuParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuParam param){
        Sku oldEntity = getOldEntity(param);
        Sku newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuResult findBySpec(SkuParam param){
        return null;
    }

    @Override
    public List<SkuResult> findListBySpec(SkuParam param){
        return null;
    }

    @Override
    public PageInfo<SkuResult> findPageBySpec(SkuParam param){
        Page<SkuResult> pageContext = getPageContext();
        IPage<SkuResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<SkuResult> param){
        List<Long> spuId = new ArrayList<>();
        List<Long> skuId = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();
        List<Long> inSkuNameList = new ArrayList<>();
        for (SkuResult skuResult : param) {
            spuId.add(skuResult.getSpuId());
            skuId.add(skuResult.getSkuId());
            List<String> skuName = Arrays.asList(skuResult.getSkuName().split(","));
            for (String s : skuName) {
                inSkuNameList.add(Long.valueOf(s));
            }
        }
        QueryWrapper<AttributeValues> attributeValuesQueryWrapper = new QueryWrapper<>();
        attributeValuesQueryWrapper.lambda().in(AttributeValues::getAttributeValuesId,inSkuNameList);
        List<AttributeValues> attributeValuesList = inSkuNameList.size() == 0 ? new ArrayList<>(): attributeValuesService.list(attributeValuesQueryWrapper);
        //查询商品名称
        QueryWrapper<Spu> spuQueryWrapper = new QueryWrapper<>();
        spuQueryWrapper.lambda().in(Spu::getSpuId,spuId);
        List<Spu> spuList =  spuId.size() == 0 ? new ArrayList<>() :spuService.list(spuQueryWrapper);

        for (Spu spu : spuList) {
            categoryIds.add(spu.getCategoryId());
        }

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Category::getCategoryId,categoryIds);
        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.list(queryWrapper);

        for (SkuResult skuResult : param) {
            for (Spu spu : spuList) {
                if (skuResult.getSpuId().equals(spu.getSpuId())) {
                    skuResult.setSpuName(spu.getName());
                    for (Category category : categoryList) {
                        if (spu.getCategoryId().equals(category.getCategoryId())){
                            skuResult.setCategoryName(category.getCategoryName());
                        }
                    }
                }
            }
            StringBuffer stringBuffer = new StringBuffer();
            List<String> resultSkuName = Arrays.asList(skuResult.getSkuName().split(","));
            for (String s : resultSkuName) {
                for (AttributeValues attributeValues : attributeValuesList) {
                    if (s.equals(attributeValues.getAttributeValuesId().toString())) {
                        stringBuffer.append(attributeValues.getAttributeValues()+",");
                    }
                }
            }
            if (stringBuffer.length()>1) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            skuResult.setCategoryName(stringBuffer.toString());

        }


    }

    private Serializable getKey(SkuParam param){
        return param.getSkuId();
    }

    private Page<SkuResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Sku getOldEntity(SkuParam param) {
        return this.getById(getKey(param));
    }

    private Sku getEntity(SkuParam param) {
        Sku entity = new Sku();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
