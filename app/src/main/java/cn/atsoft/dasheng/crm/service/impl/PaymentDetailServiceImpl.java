package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.mapper.PaymentDetailMapper;
import cn.atsoft.dasheng.crm.model.params.PaymentDetailParam;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import  cn.atsoft.dasheng.crm.service.PaymentDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 付款详情 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service
public class PaymentDetailServiceImpl extends ServiceImpl<PaymentDetailMapper, PaymentDetail> implements PaymentDetailService {

    @Override
    public void add(PaymentDetailParam param){
        PaymentDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PaymentDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PaymentDetailParam param){
        PaymentDetail oldEntity = getOldEntity(param);
        PaymentDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PaymentDetailResult findBySpec(PaymentDetailParam param){
        return null;
    }

    @Override
    public List<PaymentDetailResult> findListBySpec(PaymentDetailParam param){
        return null;
    }

    @Override
    public PageInfo<PaymentDetailResult> findPageBySpec(PaymentDetailParam param){
        Page<PaymentDetailResult> pageContext = getPageContext();
        IPage<PaymentDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PaymentDetailParam param){
        return param.getDetailId();
    }

    private Page<PaymentDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PaymentDetail getOldEntity(PaymentDetailParam param) {
        return this.getById(getKey(param));
    }

    private PaymentDetail getEntity(PaymentDetailParam param) {
        PaymentDetail entity = new PaymentDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
