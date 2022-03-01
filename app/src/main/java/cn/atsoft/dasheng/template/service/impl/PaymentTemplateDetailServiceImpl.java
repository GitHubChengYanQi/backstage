package cn.atsoft.dasheng.template.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplateDetail;
import cn.atsoft.dasheng.template.mapper.PaymentTemplateDetailMapper;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateDetailParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateDetailResult;
import cn.atsoft.dasheng.template.service.PaymentTemplateDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 付款模板详情 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
@Service
public class PaymentTemplateDetailServiceImpl extends ServiceImpl<PaymentTemplateDetailMapper, PaymentTemplateDetail> implements PaymentTemplateDetailService {

    @Override
    public void add(PaymentTemplateDetailParam param) {
        PaymentTemplateDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PaymentTemplateDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PaymentTemplateDetailParam param) {
        PaymentTemplateDetail oldEntity = getOldEntity(param);
        PaymentTemplateDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PaymentTemplateDetailResult findBySpec(PaymentTemplateDetailParam param) {
        return null;
    }

    @Override
    public List<PaymentTemplateDetailResult> findListBySpec(PaymentTemplateDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<PaymentTemplateDetailResult> findPageBySpec(PaymentTemplateDetailParam param) {
        Page<PaymentTemplateDetailResult> pageContext = getPageContext();
        IPage<PaymentTemplateDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PaymentTemplateDetailParam param) {
        return param.getDetailId();
    }

    private Page<PaymentTemplateDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PaymentTemplateDetail getOldEntity(PaymentTemplateDetailParam param) {
        return this.getById(getKey(param));
    }

    private PaymentTemplateDetail getEntity(PaymentTemplateDetailParam param) {
        PaymentTemplateDetail entity = new PaymentTemplateDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<PaymentTemplateDetailResult> getDetails(Long id) {
        List<PaymentTemplateDetail> templateDetails = this.query().eq("template_id", id).eq("display", 1).list();
        if (ToolUtil.isEmpty(templateDetails)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(templateDetails, PaymentTemplateDetailResult.class, new CopyOptions());

    }


    /**
     * 批量添加
     *
     * @param id
     * @param params
     */
    @Override
    public void addList(Long id, List<PaymentTemplateDetailParam> params) {

        List<PaymentTemplateDetail> detailList = new ArrayList<>();

        for (PaymentTemplateDetailParam param : params) {
            PaymentTemplateDetail detail = new PaymentTemplateDetail();
            ToolUtil.copyProperties(param, detail);
            detail.setTemplateId(id);
            detailList.add(detail);
        }
        this.saveBatch(detailList);
    }
}
