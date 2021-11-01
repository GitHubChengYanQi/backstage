package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.entity.ProductionStationClass;
import cn.atsoft.dasheng.production.entity.WorkingProcedure;
import cn.atsoft.dasheng.production.mapper.WorkingProcedureMapper;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureResult;
import cn.atsoft.dasheng.production.service.ProductionStationClassService;
import cn.atsoft.dasheng.production.service.ProductionStationService;
import  cn.atsoft.dasheng.production.service.WorkingProcedureService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
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
 * 工序表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
@Service
public class WorkingProcedureServiceImpl extends ServiceImpl<WorkingProcedureMapper, WorkingProcedure> implements WorkingProcedureService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductionStationService productionStationService;
    @Override
    public void add(WorkingProcedureParam param){
        WorkingProcedure entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WorkingProcedureParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WorkingProcedureParam param){
        WorkingProcedure oldEntity = getOldEntity(param);
        WorkingProcedure newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WorkingProcedureResult findBySpec(WorkingProcedureParam param){
        return null;
    }

    @Override
    public List<WorkingProcedureResult> findListBySpec(WorkingProcedureParam param){
        return null;
    }

    @Override
    public PageInfo<WorkingProcedureResult> findPageBySpec(WorkingProcedureParam param){
        Page<WorkingProcedureResult> pageContext = getPageContext();
        IPage<WorkingProcedureResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    private void format(List<WorkingProcedureResult> param){
        List<Long> userIds = new ArrayList<>();
        List<Long> productionStationIds = new ArrayList<>();
        for (WorkingProcedureResult workingProcedureResult : param) {
            userIds.add(workingProcedureResult.getUserId());
            productionStationIds.add(workingProcedureResult.getProductionStationId());
        }
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        List<ProductionStation> productionStationClassList = productionStationIds.size() == 0 ? new ArrayList<>() : productionStationService.lambdaQuery().in(ProductionStation::getProductionStationId, productionStationIds).list();
        for (WorkingProcedureResult workingProcedureResult : param) {
            for (User user : userList) {
                if (workingProcedureResult.getUserId().equals(user.getUserId())) {
                    workingProcedureResult.setUserName(user.getName());
                }
            }
            for (ProductionStation productionStation : productionStationClassList) {
                if (productionStation.getProductionStationId().equals(workingProcedureResult.getWorkingProcedureId())) {
                    workingProcedureResult.setWorkingProcedureName(productionStation.getName());
                }
            }
        }
    }

    private Serializable getKey(WorkingProcedureParam param){
        return param.getWorkingProcedureId();
    }

    private Page<WorkingProcedureResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WorkingProcedure getOldEntity(WorkingProcedureParam param) {
        return this.getById(getKey(param));
    }

    private WorkingProcedure getEntity(WorkingProcedureParam param) {
        WorkingProcedure entity = new WorkingProcedure();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
