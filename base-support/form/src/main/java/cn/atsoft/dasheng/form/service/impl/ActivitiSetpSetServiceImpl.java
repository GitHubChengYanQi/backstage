package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.mapper.ActivitiSetpSetMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import  cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.omg.CORBA.LongHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工序步骤设置表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiSetpSetServiceImpl extends ServiceImpl<ActivitiSetpSetMapper, ActivitiSetpSet> implements ActivitiSetpSetService {

    @Override
    public void add(ActivitiSetpSetParam param){
        ActivitiSetpSet entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ActivitiSetpSetParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ActivitiSetpSetParam param){
        ActivitiSetpSet oldEntity = getOldEntity(param);
        ActivitiSetpSet newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ActivitiSetpSetResult findBySpec(ActivitiSetpSetParam param){
        return null;
    }

    @Override
    public List<ActivitiSetpSetResult> findListBySpec(ActivitiSetpSetParam param){
        return null;
    }

    @Override
    public PageInfo<ActivitiSetpSetResult> findPageBySpec(ActivitiSetpSetParam param){
        Page<ActivitiSetpSetResult> pageContext = getPageContext();
        IPage<ActivitiSetpSetResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiSetpSetParam param){
        return param.getSetId();
    }

    private Page<ActivitiSetpSetResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiSetpSet getOldEntity(ActivitiSetpSetParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiSetpSet getEntity(ActivitiSetpSetParam param) {
        ActivitiSetpSet entity = new ActivitiSetpSet();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public List<ActivitiSetpSetResult> getResultByStepsId(List<Long> stepsIds){
        List<ActivitiSetpSet> activitiSetpSets = stepsIds.size() == 0 ? new ArrayList<>() : this.query().in("setps_id", stepsIds).eq("display", 1).list();
        List<ActivitiSetpSetResult> results = new ArrayList<>();
        for (ActivitiSetpSet activitiSetpSet : activitiSetpSets) {
            ActivitiSetpSetResult result = new ActivitiSetpSetResult();
            ToolUtil.copyProperties(activitiSetpSet,result);
            results.add(result);
        }
        return results;
    }

}
