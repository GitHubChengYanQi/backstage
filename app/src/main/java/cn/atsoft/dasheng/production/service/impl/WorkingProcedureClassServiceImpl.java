package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingProcedureClass;
import cn.atsoft.dasheng.production.mapper.WorkingPorcedureClassMapper;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureClassResult;
import cn.atsoft.dasheng.production.service.WorkingProcedureClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序分类表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
@Service
public class WorkingProcedureClassServiceImpl extends ServiceImpl<WorkingPorcedureClassMapper, WorkingProcedureClass> implements WorkingProcedureClassService {

    @Override
    public void add(WorkingProcedureClassParam param){
        WorkingProcedureClass entity = getEntity(param);
        this.save(entity);
    }

    @BussinessLog
    @Override
    public void delete(WorkingProcedureClassParam param){
        this.removeById(getKey(param));
    }

    @BussinessLog
    @Override
    public void update(WorkingProcedureClassParam param){
        WorkingProcedureClass oldEntity = getOldEntity(param);
        WorkingProcedureClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WorkingProcedureClassResult findBySpec(WorkingProcedureClassParam param){
        return null;
    }

    @Override
    public List<WorkingProcedureClassResult> findListBySpec(WorkingProcedureClassParam param){
        return null;
    }

    @Override
    public PageInfo<WorkingProcedureClassResult> findPageBySpec(WorkingProcedureClassParam param){
        Page<WorkingProcedureClassResult> pageContext = getPageContext();
        IPage<WorkingProcedureClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WorkingProcedureClassParam param){
        return param.getWpClassId();
    }

    private Page<WorkingProcedureClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WorkingProcedureClass getOldEntity(WorkingProcedureClassParam param) {
        return this.getById(getKey(param));
    }

    private WorkingProcedureClass getEntity(WorkingProcedureClassParam param) {
        WorkingProcedureClass entity = new WorkingProcedureClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
