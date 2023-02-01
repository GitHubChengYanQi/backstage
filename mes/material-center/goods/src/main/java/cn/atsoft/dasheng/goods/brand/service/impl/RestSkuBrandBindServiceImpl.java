package cn.atsoft.dasheng.goods.brand.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.brand.entity.RestSkuBrandBind;
import cn.atsoft.dasheng.goods.brand.mapper.RestSkuBrandBindMapper;
import cn.atsoft.dasheng.goods.brand.model.params.RestSkuBrandBindParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestSkuBrandBindResult;
import cn.atsoft.dasheng.goods.brand.service.RestSkuBrandBindService;
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
public class RestSkuBrandBindServiceImpl extends ServiceImpl<RestSkuBrandBindMapper, RestSkuBrandBind> implements RestSkuBrandBindService {

    @Override
    public void add(RestSkuBrandBindParam param){
        RestSkuBrandBind entity = getEntity(param);
        this.save(entity);

    }
    @Override
    public void addBatch(RestSkuBrandBindParam param){
        if (ToolUtil.isNotEmpty(param.getBrandIds()) && ToolUtil.isNotEmpty(param.getSkuId())){
            List<RestSkuBrandBind> entityList = new ArrayList<>();
            for (Long brandId : param.getBrandIds()) {
                RestSkuBrandBind skuBrandBind = new RestSkuBrandBind();
                skuBrandBind.setBrandId(brandId);
                skuBrandBind.setSkuId(param.getSkuId());
                entityList.add(skuBrandBind);
            }
            this.saveBatch(entityList);
        }
    }

    @Override
    public void addBatchByBrand(RestSkuBrandBindParam param) {
        if (ToolUtil.isNotEmpty(param.getSkuIds()) && ToolUtil.isNotEmpty(param.getBrandId())){
            List<RestSkuBrandBind> binds = new ArrayList<>();
            for (Long skuId : param.getSkuIds()) {
                RestSkuBrandBind skuBrandBind = new RestSkuBrandBind();
                skuBrandBind.setBrandId(param.getBrandId());
                skuBrandBind.setSkuId(skuId);
                binds.add(skuBrandBind);
            }
            this.saveBatch(binds);
        }
    }

    @Override
    public void delete(RestSkuBrandBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RestSkuBrandBindParam param){
        RestSkuBrandBind oldEntity = getOldEntity(param);
        RestSkuBrandBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestSkuBrandBindResult findBySpec(RestSkuBrandBindParam param){
        return null;
    }

    @Override
    public List<RestSkuBrandBindResult> findListBySpec(RestSkuBrandBindParam param){
        return null;
    }

    @Override
    public PageInfo<RestSkuBrandBindResult> findPageBySpec(RestSkuBrandBindParam param){
        Page<RestSkuBrandBindResult> pageContext = getPageContext();
        IPage<RestSkuBrandBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestSkuBrandBindParam param){
        return param.getSkuBrandBind();
    }

    private Page<RestSkuBrandBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestSkuBrandBind getOldEntity(RestSkuBrandBindParam param) {
        return this.getById(getKey(param));
    }

    private RestSkuBrandBind getEntity(RestSkuBrandBindParam param) {
        RestSkuBrandBind entity = new RestSkuBrandBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
