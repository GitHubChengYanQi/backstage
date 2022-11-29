package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.entity.PaymentRecord;
import cn.atsoft.dasheng.crm.mapper.PaymentRecordMapper;
import cn.atsoft.dasheng.crm.model.params.PaymentRecordParam;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import cn.atsoft.dasheng.crm.service.PaymentDetailService;
import cn.atsoft.dasheng.crm.service.PaymentRecordService;
import cn.atsoft.dasheng.crm.service.PaymentService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
    public PaymentRecord add(PaymentRecordParam param) {
        PaymentRecord entity = getEntity(param);
        this.save(entity);
        return entity;
    }


    @Override
    public void delete(PaymentRecordParam param) {
        if (ToolUtil.isEmpty(param.getRecordId())){
            throw new ServiceException(500,"所删除的目标不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void obsolete(PaymentRecordParam param) {

        if (ToolUtil.isEmpty(param.getRecordId())){
            throw new ServiceException(500,"所作废的目标不存在");
        }else {
            PaymentRecord oldEntity = getOldEntity(param);
            if (ToolUtil.isEmpty(oldEntity)){
                throw new ServiceException(500,"未找到此数据");
            }
            oldEntity.setStatus(50);
            oldEntity.setRecordId(param.getRecordId());
            this.updateById(oldEntity);
        }
    }

    @Override
    public void update(PaymentRecordParam param) {
        PaymentRecord oldEntity = getOldEntity(param);
        PaymentRecord newEntity = getEntity(param);
        newEntity.setDisplay(null);
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

    private void updatePayDetail(Long detailId) {
        PaymentDetail detail = detailService.getById(detailId);

        List<PaymentRecord> records = this.query().eq("detail_id", detailId).list();
        int money = 0;
        for (PaymentRecord record : records) {
            money = money + record.getPaymentAmount();
        }
        detail.setRealPay(money);
        if (money == detail.getMoney()) {
            detail.setStatus(99);
        }
        detailService.updateById(detail);  //更新详情状态
        paymentService.updatePay(detail.getPaymentId());
    }
}
