package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.InvoiceBill;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.mapper.InvoiceBillMapper;
import cn.atsoft.dasheng.crm.model.params.InvoiceBillParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceBillResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import  cn.atsoft.dasheng.crm.service.InvoiceBillService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.atsoft.dasheng.model.exception.ServiceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
@Service
public class InvoiceBillServiceImpl extends ServiceImpl<InvoiceBillMapper, InvoiceBill> implements InvoiceBillService {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private OrderService orderService;

    @Override
    public InvoiceBill add(InvoiceBillParam param){
        if (ToolUtil.isEmpty(param.getInvoiceBillNo())){
            throw new ServiceException(500,"发票号不可为空");
        }
        InvoiceBill entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(InvoiceBillParam param){
        if (ToolUtil.isEmpty(param.getInvoiceBillId())){
            throw new ServiceException(500,"所删除的目标不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(InvoiceBillParam param){
        InvoiceBill oldEntity = getOldEntity(param);
        InvoiceBill newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InvoiceBillResult findBySpec(InvoiceBillParam param){
        return null;
    }

    @Override
    public List<InvoiceBillResult> findListBySpec(InvoiceBillParam param){
        return null;
    }

    @Override
    public PageInfo<InvoiceBillResult> findPageBySpec(InvoiceBillParam param){
        Page<InvoiceBillResult> pageContext = getPageContext();
        IPage<InvoiceBillResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<InvoiceBillResult> param){
        List<Long> mediaIds = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        for (InvoiceBillResult invoiceBillResult : param) {
            if (ToolUtil.isNotEmpty(invoiceBillResult.getEnclosureId())){
                mediaIds.addAll(Arrays.stream(invoiceBillResult.getEnclosureId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
                orderIds.add(invoiceBillResult.getOrderId());
            }
            List<MediaUrlResult> mediaList = mediaIds.size() == 0 ? new ArrayList<>() : mediaService.getMediaUrlResults(mediaIds);
            List<Order> orderList = orderIds.size() == 0 ? new ArrayList<>() : orderService.listByIds(orderIds);
            List<OrderResult> orderResults = BeanUtil.copyToList(orderList,OrderResult.class, new CopyOptions());
            orderService.format(orderResults);
            for (InvoiceBillResult billResult : param) {
                if (ToolUtil.isNotEmpty(billResult.getEnclosureId())) {
                    List<Long> mediaIdList = Arrays.stream(invoiceBillResult.getEnclosureId().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    List<MediaUrlResult> results = new ArrayList<>();
                    for (MediaUrlResult media : mediaList) {
                        for (Long mediaId : mediaIdList) {
                            if (mediaId.equals(media.getMediaId())) {
                                results.add(media);
                                break;
                            }
                        }

                    }
                    billResult.setMediaUrlResults(results);
                }
                if (ToolUtil.isNotEmpty(billResult.getOrderId())) {
                    for (OrderResult orderResult : orderResults) {
                        if (billResult.getOrderId().equals(orderResult.getOrderId())) {
                            billResult.setOrderResult(orderResult);
                            break;
                        }
                    }
                }
            }
        }

    }

    private Serializable getKey(InvoiceBillParam param){
        return param.getInvoiceBillId();
    }

    private Page<InvoiceBillResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InvoiceBill getOldEntity(InvoiceBillParam param) {
        return this.getById(getKey(param));
    }

    private InvoiceBill getEntity(InvoiceBillParam param) {
        InvoiceBill entity = new InvoiceBill();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
