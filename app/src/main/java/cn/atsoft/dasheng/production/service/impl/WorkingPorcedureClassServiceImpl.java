package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingPorcedureClass;
import cn.atsoft.dasheng.production.mapper.WorkingPorcedureClassMapper;
import cn.atsoft.dasheng.production.model.params.WorkingPorcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingPorcedureClassResult;
import  cn.atsoft.dasheng.production.service.WorkingPorcedureClassService;
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
public class WorkingPorcedureClassServiceImpl extends ServiceImpl<WorkingPorcedureClassMapper, WorkingPorcedureClass> implements WorkingPorcedureClassService {

    @Override
    public void add(WorkingPorcedureClassParam param){
        WorkingPorcedureClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WorkingPorcedureClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WorkingPorcedureClassParam param){
        WorkingPorcedureClass oldEntity = getOldEntity(param);
        WorkingPorcedureClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WorkingPorcedureClassResult findBySpec(WorkingPorcedureClassParam param){
        return null;
    }

    @Override
    public List<WorkingPorcedureClassResult> findListBySpec(WorkingPorcedureClassParam param){
        return null;
    }

    @Override
    public PageInfo<WorkingPorcedureClassResult> findPageBySpec(WorkingPorcedureClassParam param){
        Page<WorkingPorcedureClassResult> pageContext = getPageContext();
        IPage<WorkingPorcedureClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WorkingPorcedureClassParam param){
        return param.getWpClassId();
    }

    private Page<WorkingPorcedureClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WorkingPorcedureClass getOldEntity(WorkingPorcedureClassParam param) {
        return this.getById(getKey(param));
    }

    private WorkingPorcedureClass getEntity(WorkingPorcedureClassParam param) {
        WorkingPorcedureClass entity = new WorkingPorcedureClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
