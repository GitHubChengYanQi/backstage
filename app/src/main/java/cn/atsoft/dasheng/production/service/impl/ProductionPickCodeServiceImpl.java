package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCode;
import cn.atsoft.dasheng.production.mapper.ProductionPickCodeMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeResult;
import  cn.atsoft.dasheng.production.service.ProductionPickCodeService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领取物料码 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2022-03-29
 */
@Service
public class ProductionPickCodeServiceImpl extends ServiceImpl<ProductionPickCodeMapper, ProductionPickCode> implements ProductionPickCodeService {

    @Override
    public void add(ProductionPickCodeParam param){
        ProductionPickCode entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickCodeParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickCodeParam param){
        ProductionPickCode oldEntity = getOldEntity(param);
        ProductionPickCode newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickCodeResult findBySpec(ProductionPickCodeParam param){
        return null;
    }

    @Override
    public List<ProductionPickCodeResult> findListBySpec(ProductionPickCodeParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPickCodeResult> findPageBySpec(ProductionPickCodeParam param){
        Page<ProductionPickCodeResult> pageContext = getPageContext();
        IPage<ProductionPickCodeResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPickCodeParam param){
        return null;
    }

    private Page<ProductionPickCodeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickCode getOldEntity(ProductionPickCodeParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickCode getEntity(ProductionPickCodeParam param) {
        ProductionPickCode entity = new ProductionPickCode();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
