package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.PaymentRecord;
import cn.atsoft.dasheng.crm.mapper.PaymentRecordMapper;
import cn.atsoft.dasheng.crm.model.params.PaymentRecordParam;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import cn.atsoft.dasheng.crm.service.PaymentDetailService;
import cn.atsoft.dasheng.crm.service.PaymentRecordService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.PaymentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 付款记录 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-03-01
 */
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements PaymentRecordService {

    @Autowired
    private PaymentDetailService detailService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public void add(PaymentRecordParam param) {

        PaymentRecord entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PaymentRecordParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PaymentRecordParam param) {
        PaymentRecord oldEntity = getOldEntity(param);
        PaymentRecord newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PaymentRecordResult findBySpec(PaymentRecordParam param) {
        return null;
    }

    @Override
    public List<PaymentRecordResult> findListBySpec(PaymentRecordParam param) {
        return null;
    }

    @Override
    public PageInfo<PaymentRecordResult> findPageBySpec(PaymentRecordParam param) {
        Page<PaymentRecordResult> pageContext = getPageContext();
        IPage<PaymentRecordResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PaymentRecordParam param) {
        return param.getRecordId();
    }

    private Page<PaymentRecordResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PaymentRecord getOldEntity(PaymentRecordParam param) {
        return this.getById(getKey(param));
    }

    private PaymentRecord getEntity(PaymentRecordParam param) {
        PaymentRecord entity = new PaymentRecord();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
