package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.mapper.ProductionStationMapper;
import cn.atsoft.dasheng.production.model.params.ProductionStationParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import  cn.atsoft.dasheng.production.service.ProductionStationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工位表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class ProductionStationServiceImpl extends ServiceImpl<ProductionStationMapper, ProductionStation> implements ProductionStationService {

    @Override
    public void add(ProductionStationParam param){
        ProductionStation entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionStationParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionStationParam param){
        ProductionStation oldEntity = getOldEntity(param);
        ProductionStation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionStationResult findBySpec(ProductionStationParam param){
        return null;
    }

    @Override
    public List<ProductionStationResult> findListBySpec(ProductionStationParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionStationResult> findPageBySpec(ProductionStationParam param){
        Page<ProductionStationResult> pageContext = getPageContext();
        IPage<ProductionStationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionStationParam param){
        return param.getProductionStationId();
    }

    private Page<ProductionStationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionStation getOldEntity(ProductionStationParam param) {
        return this.getById(getKey(param));
    }

    private ProductionStation getEntity(ProductionStationParam param) {
        ProductionStation entity = new ProductionStation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
