package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceCycle;
import cn.atsoft.dasheng.erp.mapper.MaintenanceCycleMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceCycleParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceCycleResult;
import  cn.atsoft.dasheng.erp.service.MaintenanceCycleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 物料维护周期 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-08
 */
@Service
public class MaintenanceCycleServiceImpl extends ServiceImpl<MaintenanceCycleMapper, MaintenanceCycle> implements MaintenanceCycleService {

    @Override
    public void add(MaintenanceCycleParam param){
        MaintenanceCycle entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MaintenanceCycleParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceCycleParam param){
        MaintenanceCycle oldEntity = getOldEntity(param);
        MaintenanceCycle newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaintenanceCycleResult findBySpec(MaintenanceCycleParam param){
        return null;
    }

    @Override
    public List<MaintenanceCycleResult> findListBySpec(MaintenanceCycleParam param){
        return null;
    }

    @Override
    public PageInfo<MaintenanceCycleResult> findPageBySpec(MaintenanceCycleParam param){
        Page<MaintenanceCycleResult> pageContext = getPageContext();
        IPage<MaintenanceCycleResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceCycleParam param){
        return param.getMaintenanceCycleId();
    }

    private Page<MaintenanceCycleResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private MaintenanceCycle getOldEntity(MaintenanceCycleParam param) {
        return this.getById(getKey(param));
    }

    private MaintenanceCycle getEntity(MaintenanceCycleParam param) {
        MaintenanceCycle entity = new MaintenanceCycle();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
