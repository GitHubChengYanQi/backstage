package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
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
 * 流程主表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiProcessServiceImpl extends ServiceImpl<ActivitiProcessMapper, ActivitiProcess> implements ActivitiProcessService {

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Override
    public void add(ActivitiProcessParam param) {
        ActivitiProcess entity = getEntity(param);
        this.save(entity);
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> userIds = new ArrayList<>();
        wxCpTemplate.setUserIds(userIds);
        wxCpSendTemplate.sendTemplate();
        String url = param.getUrl();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("有新的流程审批注意查看");
        wxCpTemplate.setDescription("有新的流程审批注意查看");
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
    }

    @Override
    public void delete(ActivitiProcessParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ActivitiProcessParam param) {
        ActivitiProcess oldEntity = getOldEntity(param);
        ActivitiProcess newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ActivitiProcessResult findBySpec(ActivitiProcessParam param) {
        return null;
    }

    @Override
    public List<ActivitiProcessResult> findListBySpec(ActivitiProcessParam param) {
        return null;
    }

    @Override
    public PageInfo<ActivitiProcessResult> findPageBySpec(ActivitiProcessParam param) {
        Page<ActivitiProcessResult> pageContext = getPageContext();
        IPage<ActivitiProcessResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiProcessParam param) {
        return param.getProcessId();
    }

    private Page<ActivitiProcessResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiProcess getOldEntity(ActivitiProcessParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiProcess getEntity(ActivitiProcessParam param) {
        ActivitiProcess entity = new ActivitiProcess();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
