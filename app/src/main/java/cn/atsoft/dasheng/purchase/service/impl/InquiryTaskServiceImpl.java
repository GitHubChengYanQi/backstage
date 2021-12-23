package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.mapper.InquiryTaskMapper;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskResult;
import  cn.atsoft.dasheng.purchase.service.InquiryTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 询价任务 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Service
public class InquiryTaskServiceImpl extends ServiceImpl<InquiryTaskMapper, InquiryTask> implements InquiryTaskService {

    @Override
    public void add(InquiryTaskParam param){
        InquiryTask entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InquiryTaskParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(InquiryTaskParam param){
        InquiryTask oldEntity = getOldEntity(param);
        InquiryTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InquiryTaskResult findBySpec(InquiryTaskParam param){
        return null;
    }

    @Override
    public List<InquiryTaskResult> findListBySpec(InquiryTaskParam param){
        return null;
    }

    @Override
    public PageInfo<InquiryTaskResult> findPageBySpec(InquiryTaskParam param){
        Page<InquiryTaskResult> pageContext = getPageContext();
        IPage<InquiryTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InquiryTaskParam param){
        return param.getInquiryTaskId();
    }

    private Page<InquiryTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InquiryTask getOldEntity(InquiryTaskParam param) {
        return this.getById(getKey(param));
    }

    private InquiryTask getEntity(InquiryTaskParam param) {
        InquiryTask entity = new InquiryTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
