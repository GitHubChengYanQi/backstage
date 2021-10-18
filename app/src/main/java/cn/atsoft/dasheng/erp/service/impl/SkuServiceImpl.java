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
    private SkuValuesService skuValuesService;
    @Autowired
    private ItemAttributeService itemAttributeService;

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
        ids.toArray();
        for (SkuValues skuValue : param.getSkuValues()) {
           stringBuffer.append(skuValue.getAttributeValuesId()+",");

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
        List<Long> skuValueId = new ArrayList<>();
        for (SkuResult skuResult : param) {
            spuId.add(skuResult.getSpuId());
            skuId.add(skuResult.getSkuId());
        }
        //查询商品名称
        QueryWrapper<Spu> spuQueryWrapper = new QueryWrapper<>();
        spuQueryWrapper.lambda().in(Spu::getSpuId,spuId);
        List<Spu> spuList =  spuId.size() == 0 ? new ArrayList<>() :spuService.list(spuQueryWrapper);


        //查询sku明细表
        QueryWrapper<SkuValues> skuValuesQueryWrapper = new QueryWrapper<>();
        spuQueryWrapper.lambda().in(Spu::getSpuId,spuId);
        List<SkuValues> skuValuesList =spuId.size() == 0 ? new ArrayList<>() :  skuValuesService.list(skuValuesQueryWrapper);
        List<Long> skuValuesIds = new ArrayList<>();
        for (SkuValues skuValues : skuValuesList) {
            skuValuesIds.add(skuValues.getAttributeValuesId());
        }
        QueryWrapper<AttributeValues> attributeValuesQueryWrapper = new QueryWrapper<>();
        spuQueryWrapper.lambda().in(Spu::getSpuId,spuId);
        List<AttributeValues> attributeValuesList =spuId.size() == 0 ? new ArrayList<>() : attributeValuesService.list(attributeValuesQueryWrapper);



        List<Long> atteibuteIds = new ArrayList<>();
        for (SkuValues skuValues : skuValuesList) {
            atteibuteIds.add(skuValues.getAttributeId());
        }
        //查询属性名称
        QueryWrapper<ItemAttribute> itemAttributeQueryWrapper = new QueryWrapper<>();
        itemAttributeQueryWrapper.lambda().in(ItemAttribute::getAttributeId,atteibuteIds);
        List<ItemAttribute> attributelist =atteibuteIds.size() == 0 ? new ArrayList<>() :  itemAttributeService.list(itemAttributeQueryWrapper);





        for (SkuResult skuResult : param) {
            for (Spu spu : spuList) {
                if (skuResult.getSpuId().equals(spu.getSpuId())) {
                    skuResult.setSpuName(spu.getName());
                }
            }
            for (ItemAttribute itemAttribute : attributelist) {
            }
            List<SkuValuesResult> skuValuesResults = new ArrayList<>();
            for (SkuValues skuValues : skuValuesList) {
                if (skuResult.getSkuId().equals(skuValues.getSkuId())) {
                    SkuValuesResult skuValuesResult = new SkuValuesResult();
                    ToolUtil.copyProperties(skuValues,skuValuesResult);
                    for (AttributeValues attributeValues : attributeValuesList) {
                        if (attributeValues.getAttributeId().equals(skuValues.getAttributeId())) {
                            AttributeValuesResult attributeValuesResult = new AttributeValuesResult();
                            ToolUtil.copyProperties(attributeValues,attributeValuesResult);
                            skuValuesResult.setAttributeValuesResult(attributeValuesResult);
                        }
                    }
                    skuValuesResults.add(skuValuesResult);
                }
            }
            skuResult.setSkuValuesResults(skuValuesResults);

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
