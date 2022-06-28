package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.mapper.MaintenanceDetailMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceDetailResult;
import  cn.atsoft.dasheng.erp.service.MaintenanceDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 养护申请子表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@Service
public class MaintenanceDetailServiceImpl extends ServiceImpl<MaintenanceDetailMapper, MaintenanceDetail> implements MaintenanceDetailService {

    @Override
    public void add(MaintenanceDetailParam param){
        MaintenanceDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MaintenanceDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceDetailParam param){
        MaintenanceDetail oldEntity = getOldEntity(param);
        MaintenanceDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaintenanceDetailResult findBySpec(MaintenanceDetailParam param){
        return null;
    }

    @Override
    public List<MaintenanceDetailResult> findListBySpec(MaintenanceDetailParam param){
        return null;
    }

    @Override
    public PageInfo<MaintenanceDetailResult> findPageBySpec(MaintenanceDetailParam param){
        Page<MaintenanceDetailResult> pageContext = getPageContext();
        IPage<MaintenanceDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceDetailParam param){
        return param.getMaintenanceDetailId();
    }

    private Page<MaintenanceDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private MaintenanceDetail getOldEntity(MaintenanceDetailParam param) {
        return this.getById(getKey(param));
    }

    private MaintenanceDetail getEntity(MaintenanceDetailParam param) {
        MaintenanceDetail entity = new MaintenanceDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
