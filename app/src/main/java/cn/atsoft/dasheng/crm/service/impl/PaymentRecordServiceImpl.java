package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.entity.PaymentRecord;
import cn.atsoft.dasheng.crm.mapper.PaymentRecordMapper;
import cn.atsoft.dasheng.crm.model.params.PaymentRecordParam;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.crm.service.PaymentDetailService;
import cn.atsoft.dasheng.crm.service.PaymentRecordService;
import cn.atsoft.dasheng.crm.service.PaymentService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderService orderService;
    @Autowired
    private MediaService mediaService;

    @Override
    @Transactional
    public PaymentRecord add(PaymentRecordParam param) {
        PaymentRecord entity = getEntity(param);
        this.save(entity);
        return entity;
    }
    @Override
    public List<PaymentRecord> listByOrderIds(List<Long> orderIds){
        if (ToolUtil.isEmpty(orderIds) || orderIds.size() == 0){
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(PaymentRecord::getOrderId,orderIds).list();
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
        this.format(page.getRecords());
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
        Long money = 0L;
        for (PaymentRecord record : records) {
            money = money + record.getPaymentAmount();
        }
        detail.setRealPay(money);
        if (money.equals(detail.getMoney())) {
            detail.setStatus(99);
        }
        detailService.updateById(detail);  //更新详情状态
        paymentService.updatePay(detail.getPaymentId());
    }

    @Override
    public void format(List<PaymentRecordResult> results) {
        List<Long> orderIds = new ArrayList<>();
        for (PaymentRecordResult paymentRecordResult : results) {
            orderIds.add(paymentRecordResult.getOrderId());
        }
        List<Long>mediaIds = new ArrayList<>();
        for (PaymentRecordResult result : results) {
            if (ToolUtil.isNotEmpty(result.getField())){
                mediaIds.addAll(Arrays.stream(result.getField().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        }
        List<MediaUrlResult> mediaUrlResults = mediaService.getMediaUrlResults(mediaIds);
        List<Order> orderList = orderIds.size() == 0 ? new ArrayList<>() : orderService.listByIds(orderIds);
        List<OrderResult> orderResults = BeanUtil.copyToList(orderList,OrderResult.class, new CopyOptions());
        orderService.format(orderResults);
        for (PaymentRecordResult recordResult : results) {
            if (ToolUtil.isNotEmpty(recordResult.getField())) {
                List<Long> mediaIdList = Arrays.stream(recordResult.getField().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<MediaUrlResult> mediaUrlResultList = new ArrayList<>();
                for (MediaUrlResult media : mediaUrlResults) {
                    for (Long mediaId : mediaIdList) {
                        if (mediaId.equals(media.getMediaId())) {
                            mediaUrlResultList.add(media);
                            break;
                        }
                    }

                }
                recordResult.setMediaUrlResults(mediaUrlResultList);
            }
            if (ToolUtil.isNotEmpty(recordResult.getRecordId())) {
                for (OrderResult orderResult : orderResults) {
                    if (recordResult.getOrderId().equals(orderResult.getOrderId())) {
                        recordResult.setOrderResult(orderResult);
                        break;
                    }
                }
            }
        }

    }
}
