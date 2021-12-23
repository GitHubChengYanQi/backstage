package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.mapper.InquiryTaskDetailMapper;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskDetailResult;
import  cn.atsoft.dasheng.purchase.service.InquiryTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 询价任务详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Service
public class InquiryTaskDetailServiceImpl extends ServiceImpl<InquiryTaskDetailMapper, InquiryTaskDetail> implements InquiryTaskDetailService {

    @Override
    public void add(InquiryTaskDetailParam param){
        InquiryTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InquiryTaskDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(InquiryTaskDetailParam param){
        InquiryTaskDetail oldEntity = getOldEntity(param);
        InquiryTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InquiryTaskDetailResult findBySpec(InquiryTaskDetailParam param){
        return null;
    }

    @Override
    public List<InquiryTaskDetailResult> findListBySpec(InquiryTaskDetailParam param){
        return null;
    }

    @Override
    public PageInfo<InquiryTaskDetailResult> findPageBySpec(InquiryTaskDetailParam param){
        Page<InquiryTaskDetailResult> pageContext = getPageContext();
        IPage<InquiryTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InquiryTaskDetailParam param){
        return param.getInquiryDetailId();
    }

    private Page<InquiryTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InquiryTaskDetail getOldEntity(InquiryTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private InquiryTaskDetail getEntity(InquiryTaskDetailParam param) {
        InquiryTaskDetail entity = new InquiryTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
