package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.entity.ProductionStationClass;
import cn.atsoft.dasheng.production.mapper.ProductionStationMapper;
import cn.atsoft.dasheng.production.model.params.ProductionStationParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import cn.atsoft.dasheng.production.service.ProductionStationClassService;
import cn.atsoft.dasheng.production.service.ProductionStationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ProductionStationClassService stationClassService;
    @Autowired
    private UserService userService;

    @Override
    public void add(ProductionStationParam param) {
        ProductionStation entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionStationParam param) {

        ProductionStation deleteEntity = getOldEntity(param);
        deleteEntity.setDisplay(0);
        this.baseMapper.updateById(deleteEntity);
    }

    @Override
    public void update(ProductionStationParam param) {
        ProductionStation oldEntity = getOldEntity(param);
        ProductionStation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionStationResult findBySpec(ProductionStationParam param) {
        return null;
    }

    @Override
    public List<ProductionStationResult> findListBySpec(ProductionStationParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionStationResult> findPageBySpec(ProductionStationParam param) {
        Page<ProductionStationResult> pageContext = getPageContext();
        IPage<ProductionStationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionStationParam param) {
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

    public void format(List<ProductionStationResult> data) {
        List<Long> ids = new ArrayList<>();
        for (ProductionStationResult datum : data) {
            ids.add(datum.getProductionStationId());
        }
        List<ProductionStationClass> stationClasses = ids.size() == 0 ? new ArrayList<>() : stationClassService.query().in("production_station_id", ids).list();


        if (ToolUtil.isNotEmpty(stationClasses)) {
            List<Long> userIds = new ArrayList<>();
            for (ProductionStationResult datum : data) {
                for (ProductionStationClass stationClass : stationClasses) {
                    if (stationClass.getProductionStationId() != null && datum.getProductionStationId() != null &&
                            stationClass.getProductionStationId().equals(datum.getProductionStationId())) {
                        userIds.add(stationClass.getUserId());
                    }
                }
            }


        }
    }
}
