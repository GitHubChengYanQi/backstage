package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.mapper.SkuBrandBindMapper;
import cn.atsoft.dasheng.erp.model.params.SkuBrandBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuBrandBindResult;
import  cn.atsoft.dasheng.erp.service.SkuBrandBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-01-18
 */
@Service
public class SkuBrandBindServiceImpl extends ServiceImpl<SkuBrandBindMapper, SkuBrandBind> implements SkuBrandBindService {

    @Override
    public void add(SkuBrandBindParam param){
        SkuBrandBind entity = getEntity(param);
        this.save(entity);

    }
    @Override
    public void addBatch(SkuBrandBindParam param){
        if (ToolUtil.isNotEmpty(param.getBrandIds()) && ToolUtil.isNotEmpty(param.getSkuId())){
            List<SkuBrandBind> entityList = new ArrayList<>();
            for (Long brandId : param.getBrandIds()) {
                SkuBrandBind skuBrandBind = new SkuBrandBind();
                skuBrandBind.setBrandId(brandId);
                skuBrandBind.setSkuId(param.getSkuId());
                entityList.add(skuBrandBind);
            }
            this.saveBatch(entityList);
        }
    }

    @Override
    public void addBatchByBrand(SkuBrandBindParam param) {
        if (ToolUtil.isNotEmpty(param.getSkuIds()) && ToolUtil.isNotEmpty(param.getBrandId())){
            List<SkuBrandBind> binds = new ArrayList<>();
            for (Long skuId : param.getSkuIds()) {
                SkuBrandBind skuBrandBind = new SkuBrandBind();
                skuBrandBind.setBrandId(param.getBrandId());
                skuBrandBind.setSkuId(skuId);
                binds.add(skuBrandBind);
            }
            this.saveBatch(binds);
        }
    }

    @Override
    public void delete(SkuBrandBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuBrandBindParam param){
        SkuBrandBind oldEntity = getOldEntity(param);
        SkuBrandBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuBrandBindResult findBySpec(SkuBrandBindParam param){
        return null;
    }

    @Override
    public List<SkuBrandBindResult> findListBySpec(SkuBrandBindParam param){
        return null;
    }

    @Override
    public PageInfo<SkuBrandBindResult> findPageBySpec(SkuBrandBindParam param){
        Page<SkuBrandBindResult> pageContext = getPageContext();
        IPage<SkuBrandBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuBrandBindParam param){
        return param.getSkuBrandBind();
    }

    private Page<SkuBrandBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuBrandBind getOldEntity(SkuBrandBindParam param) {
        return this.getById(getKey(param));
    }

    private SkuBrandBind getEntity(SkuBrandBindParam param) {
        SkuBrandBind entity = new SkuBrandBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
