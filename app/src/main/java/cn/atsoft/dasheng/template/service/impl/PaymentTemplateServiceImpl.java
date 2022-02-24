package cn.atsoft.dasheng.template.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplate;
import cn.atsoft.dasheng.template.mapper.PaymentTemplateMapper;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateResult;
import  cn.atsoft.dasheng.template.service.PaymentTemplateService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 付款模板 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
@Service
public class PaymentTemplateServiceImpl extends ServiceImpl<PaymentTemplateMapper, PaymentTemplate> implements PaymentTemplateService {

    @Override
    public void add(PaymentTemplateParam param){
        PaymentTemplate entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PaymentTemplateParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PaymentTemplateParam param){
        PaymentTemplate oldEntity = getOldEntity(param);
        PaymentTemplate newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PaymentTemplateResult findBySpec(PaymentTemplateParam param){
        return null;
    }

    @Override
    public List<PaymentTemplateResult> findListBySpec(PaymentTemplateParam param){
        return null;
    }

    @Override
    public PageInfo<PaymentTemplateResult> findPageBySpec(PaymentTemplateParam param){
        Page<PaymentTemplateResult> pageContext = getPageContext();
        IPage<PaymentTemplateResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PaymentTemplateParam param){
        return param.getTemplateId();
    }

    private Page<PaymentTemplateResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PaymentTemplate getOldEntity(PaymentTemplateParam param) {
        return this.getById(getKey(param));
    }

    private PaymentTemplate getEntity(PaymentTemplateParam param) {
        PaymentTemplate entity = new PaymentTemplate();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
