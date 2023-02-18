package cn.atsoft.dasheng.audit.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.entity.ActivitiProcessLogDetail;
import cn.atsoft.dasheng.audit.mapper.ActivitiProcessLogDetailMapper;
import cn.atsoft.dasheng.audit.model.params.ActivitiProcessLogDetailParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogDetailResult;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程日志人员表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-02-16
 */
@Service
public class ActivitiProcessFormFormLogDetailServiceImpl extends ServiceImpl<ActivitiProcessLogDetailMapper, ActivitiProcessLogDetail> implements ActivitiProcessFormLogDetailService {

    @Override
    public void add(ActivitiProcessLogDetailParam param){
        ActivitiProcessLogDetail entity = getEntity(param);
        this.save(entity);
    }
    @Override
    public void addByLog(ActivitiProcessLog log, List<Long> userIds){
        List<ActivitiProcessLogDetail> entityList = new ArrayList<>();
        for (Long userId : userIds) {
            entityList.add(new ActivitiProcessLogDetail(){{
                ToolUtil.copyProperties(log,this);
                setUserId(userId);
            }});
        }
        this.saveBatch(entityList);
    }

    @Override
    public void delete(ActivitiProcessLogDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ActivitiProcessLogDetailParam param){
        ActivitiProcessLogDetail oldEntity = getOldEntity(param);
        ActivitiProcessLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ActivitiProcessLogDetailResult findBySpec(ActivitiProcessLogDetailParam param){
        return null;
    }

    @Override
    public List<ActivitiProcessLogDetailResult> findListBySpec(ActivitiProcessLogDetailParam param){
        return null;
    }

    @Override
    public List<ActivitiProcessLogDetail> listByLogIds(List<Long> logIds){
        if (logIds.size() == 0){
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(ActivitiProcessLogDetail::getLogId,logIds).list();
    }

    @Override
    public PageInfo<ActivitiProcessLogDetailResult> findPageBySpec(ActivitiProcessLogDetailParam param){
        Page<ActivitiProcessLogDetailResult> pageContext = getPageContext();
        IPage<ActivitiProcessLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiProcessLogDetailParam param){
        return param.getLogDetailId();
    }

    private Page<ActivitiProcessLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiProcessLogDetail getOldEntity(ActivitiProcessLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiProcessLogDetail getEntity(ActivitiProcessLogDetailParam param) {
        ActivitiProcessLogDetail entity = new ActivitiProcessLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
