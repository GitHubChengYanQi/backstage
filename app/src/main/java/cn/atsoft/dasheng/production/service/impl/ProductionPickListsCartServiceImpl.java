package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsCartMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import  cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领料单详情表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Service
public class ProductionPickListsCartServiceImpl extends ServiceImpl<ProductionPickListsCartMapper, ProductionPickListsCart> implements ProductionPickListsCartService {

    @Override
    public void add(ProductionPickListsCartParam param){
        ProductionPickListsCart entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickListsCartParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsCartParam param){
        ProductionPickListsCart oldEntity = getOldEntity(param);
        ProductionPickListsCart newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsCartResult findBySpec(ProductionPickListsCartParam param){
        return null;
    }

    @Override
    public List<ProductionPickListsCartResult> findListBySpec(ProductionPickListsCartParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPickListsCartResult> findPageBySpec(ProductionPickListsCartParam param){
        Page<ProductionPickListsCartResult> pageContext = getPageContext();
        IPage<ProductionPickListsCartResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPickListsCartParam param){
        return param.getPickListsCart();
    }

    private Page<ProductionPickListsCartResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickListsCart getOldEntity(ProductionPickListsCartParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickListsCart getEntity(ProductionPickListsCartParam param) {
        ProductionPickListsCart entity = new ProductionPickListsCart();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
