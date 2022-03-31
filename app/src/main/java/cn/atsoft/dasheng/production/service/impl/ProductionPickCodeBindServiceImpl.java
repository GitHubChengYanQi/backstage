package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCodeBind;
import cn.atsoft.dasheng.production.mapper.ProductionPickCodeBindMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeBindResult;
import  cn.atsoft.dasheng.production.service.ProductionPickCodeBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-29
 */
@Service
public class ProductionPickCodeBindServiceImpl extends ServiceImpl<ProductionPickCodeBindMapper, ProductionPickCodeBind> implements ProductionPickCodeBindService {

    @Override
    public void add(ProductionPickCodeBindParam param){
        ProductionPickCodeBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickCodeBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickCodeBindParam param){
        ProductionPickCodeBind oldEntity = getOldEntity(param);
        ProductionPickCodeBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickCodeBindResult findBySpec(ProductionPickCodeBindParam param){
        return null;
    }

    @Override
    public List<ProductionPickCodeBindResult> findListBySpec(ProductionPickCodeBindParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPickCodeBindResult> findPageBySpec(ProductionPickCodeBindParam param){
        Page<ProductionPickCodeBindResult> pageContext = getPageContext();
        IPage<ProductionPickCodeBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPickCodeBindParam param){
        return param.getPickCodeBindId();
    }

    private Page<ProductionPickCodeBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickCodeBind getOldEntity(ProductionPickCodeBindParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickCodeBind getEntity(ProductionPickCodeBindParam param) {
        ProductionPickCodeBind entity = new ProductionPickCodeBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
