package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStationClass;
import cn.atsoft.dasheng.production.mapper.ProductionStationClassMapper;
import cn.atsoft.dasheng.production.model.params.ProductionStationClassParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationClassResult;
import  cn.atsoft.dasheng.production.service.ProductionStationClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工位绑定表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class ProductionStationClassServiceImpl extends ServiceImpl<ProductionStationClassMapper, ProductionStationClass> implements ProductionStationClassService {




    @Override
    public void add(ProductionStationClassParam param){
        ProductionStationClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionStationClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionStationClassParam param){
        ProductionStationClass oldEntity = getOldEntity(param);
        ProductionStationClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionStationClassResult findBySpec(ProductionStationClassParam param){
        return null;
    }

    @Override
    public List<ProductionStationClassResult> findListBySpec(ProductionStationClassParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionStationClassResult> findPageBySpec(ProductionStationClassParam param){
        Page<ProductionStationClassResult> pageContext = getPageContext();
        IPage<ProductionStationClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionStationClassParam param){
        return param.getProductionStationClassId();
    }

    private Page<ProductionStationClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionStationClass getOldEntity(ProductionStationClassParam param) {
        return this.getById(getKey(param));
    }

    private ProductionStationClass getEntity(ProductionStationClassParam param) {
        ProductionStationClass entity = new ProductionStationClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
