package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStationBind;
import cn.atsoft.dasheng.production.mapper.ProductionStationBindMapper;
import cn.atsoft.dasheng.production.model.params.ProductionStationBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationBindResult;
import  cn.atsoft.dasheng.production.service.ProductionStationBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工位绑定表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
@Service
public class ProductionStationBindServiceImpl extends ServiceImpl<ProductionStationBindMapper, ProductionStationBind> implements ProductionStationBindService {

    @Autowired
    private UserService userService;
    @Override
    public void add(ProductionStationBindParam param){
        ProductionStationBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionStationBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionStationBindParam param){
        ProductionStationBind oldEntity = getOldEntity(param);
        ProductionStationBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionStationBindResult findBySpec(ProductionStationBindParam param){
        return null;
    }

    @Override
    public List<ProductionStationBindResult> findListBySpec(ProductionStationBindParam param){
        List<ProductionStationBindResult> bindResults = this.baseMapper.customList(param);
        this.format(bindResults);
        return bindResults;
    }
    @Override
    public List<ProductionStationBindResult> getResultsByStationIds(List<Long> ids){
        List<ProductionStationBind> stationBinds = ids.size() == 0 ? new ArrayList<>() : this.query().in("production_station_id", ids).list();
        List<ProductionStationBindResult> results = new ArrayList<>();
        for (ProductionStationBind stationBind : stationBinds) {
            ProductionStationBindResult result = new ProductionStationBindResult();
            ToolUtil.copyProperties(stationBind,result);
            results.add(result);
        }
        this.format(results);
        return results;
    }
    @Override
    public PageInfo<ProductionStationBindResult> findPageBySpec(ProductionStationBindParam param){
        Page<ProductionStationBindResult> pageContext = getPageContext();
        IPage<ProductionStationBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionStationBindParam param){
        return param.getProductionStationBindId();
    }

    private Page<ProductionStationBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionStationBind getOldEntity(ProductionStationBindParam param) {
        return this.getById(getKey(param));
    }

    private ProductionStationBind getEntity(ProductionStationBindParam param) {
        ProductionStationBind entity = new ProductionStationBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    public void format(List<ProductionStationBindResult> param){
        List<Long> userIds = new ArrayList<>();
        for (ProductionStationBindResult stationBindResult : param) {
            userIds.add(stationBindResult.getUserId());
        }
        List<UserResult> userResults = userService.getUserResultsByIds(userIds);
        for (ProductionStationBindResult stationBindResult : param) {
            for (UserResult userResult : userResults) {
                if (stationBindResult.getUserId().equals(userResult.getUserId())){
                    stationBindResult.setUserResult(userResult);
                }
            }
        }
    }


}
