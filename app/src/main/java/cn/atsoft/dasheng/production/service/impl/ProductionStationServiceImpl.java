package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.entity.ProductionStationBind;
import cn.atsoft.dasheng.production.entity.ProductionStationClass;
import cn.atsoft.dasheng.production.mapper.ProductionStationMapper;
import cn.atsoft.dasheng.production.model.params.ProductionStationParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationBindResult;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import cn.atsoft.dasheng.production.service.ProductionStationBindService;
import cn.atsoft.dasheng.production.service.ProductionStationClassService;
import cn.atsoft.dasheng.production.service.ProductionStationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    private ProductionStationBindService stationBindService;
    @Autowired
    private UserService userService;

    @Override
    public void add(ProductionStationParam param) {
        ProductionStation entity = getEntity(param);
        this.save(entity);
        /**
         * 添加绑定表数据
         */
        param.setProductionStationId(entity.getProductionStationId());
        this.saveOrUpdateBind(param);
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
        oldEntity.setDisplay(null);
        this.saveOrUpdateBind(param);
        this.updateById(newEntity);
    }

    private void saveOrUpdateBind(ProductionStationParam param){
        List<ProductionStationBind> productionStationBindList = stationBindService.query().eq("production_station_id", param.getProductionStationId()).list();
        for (ProductionStationBind stationBind : productionStationBindList) {
            stationBind.setDisplay(0);
        }
        stationBindService.updateBatchById(productionStationBindList);
        List<ProductionStationBind> bindList = new ArrayList<>();
        for (Long userId : param.getUserIds()) {
            ProductionStationBind bind = new ProductionStationBind();
            bind.setProductionStationId(param.getProductionStationId());
            bind.setUserId(userId);
            bindList.add(bind);
        }
        stationBindService.saveBatch(bindList);
    }
    @Override
    public ProductionStationResult findBySpec(ProductionStationParam param) {
        return null;
    }

    @Override
    public List<ProductionStationResult> findListBySpec(ProductionStationParam param) {
        List<ProductionStationResult> productionStationResults = this.baseMapper.customList(param);
        this.format(productionStationResults);
        return productionStationResults;
    }

    @Override
    public PageInfo<ProductionStationResult> findPageBySpec(ProductionStationParam param) {
        Page<ProductionStationResult> pageContext = getPageContext();
        IPage<ProductionStationResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
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

    @Override
    public void format(List<ProductionStationResult> data) {
        List<Long> ids = new ArrayList<>();
        for (ProductionStationResult datum : data) {
            ids.add(datum.getProductionStationId());
        }
        /**
         * 查询子表信息
         */
//        List<ProductionStationBindResult> stationBinds = stationBindService.getResultsByStationIds(ids);
        List<ProductionStationBindResult> stationBinds = stationBindService.getResultsByStationIds(ids);

        if (ToolUtil.isNotEmpty(stationBinds)) {

            for (ProductionStationResult datum : data) {
                List<ProductionStationBindResult> bindResults = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                for (ProductionStationBindResult stationBind : stationBinds) {
                    if (datum.getProductionStationId().equals(stationBind.getProductionStationId())){
                        bindResults.add(stationBind);
                        userIds.add(stationBind.getUserId());
                    }
                }
                datum.setUserIds(userIds);
                datum.setBindResults(bindResults);
            }
        }
    }
    @Override
    public List<ProductionStationResult> getResultsByIds(List<Long> ids){
        List<ProductionStation> productionStations = ids.size() == 0 ? new ArrayList<>() : this.query().in("production_station_id", ids).eq("display", 1).list();
        List<ProductionStationResult> results = new ArrayList<>();
        for (ProductionStation productionStation : productionStations) {
            ProductionStationResult result = new ProductionStationResult();
            ToolUtil.copyProperties(productionStation,result);
            results.add(result);
        }
        this.format(results);
        return results;
    }
}
