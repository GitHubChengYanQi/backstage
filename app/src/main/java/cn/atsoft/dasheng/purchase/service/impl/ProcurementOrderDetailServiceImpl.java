package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrderDetail;
import cn.atsoft.dasheng.purchase.mapper.ProcurementOrderDetailMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderDetailResult;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Service
public class ProcurementOrderDetailServiceImpl extends ServiceImpl<ProcurementOrderDetailMapper, ProcurementOrderDetail> implements ProcurementOrderDetailService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProcurementOrderService orderService;


    @Override
    public void add(ProcurementOrderDetailParam param) {
        ProcurementOrderDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProcurementOrderDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProcurementOrderDetailParam param) {
        ProcurementOrderDetail oldEntity = getOldEntity(param);
        ProcurementOrderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }


    @Override
    public ProcurementOrderDetailResult findBySpec(ProcurementOrderDetailParam param) {
        return null;
    }

    @Override
    public List<ProcurementOrderDetailResult> findListBySpec(ProcurementOrderDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcurementOrderDetailResult> findPageBySpec(ProcurementOrderDetailParam param) {
        Page<ProcurementOrderDetailResult> pageContext = getPageContext();
        IPage<ProcurementOrderDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 更新采购单总价格
     *
     * @param orderId
     */
    @Override
    public void updateMoney(Long orderId) {
        if (ToolUtil.isEmpty(orderId)) {
            throw new ServiceException(500, "采购单不正确");
        }
        List<ProcurementOrderDetail> orderDetails = this.query().eq("procurement_order_id", orderId).list();
        Integer countMoney = 0;
        for (ProcurementOrderDetail orderDetail : orderDetails) {
            countMoney = countMoney + orderDetail.getMoney();
        }
        ProcurementOrder order = new ProcurementOrder();
        order.setMoney(countMoney);
        orderService.update(order, new QueryWrapper<ProcurementOrder>() {{
            eq("procurement_order_id", orderId);
        }});
    }

    private Serializable getKey(ProcurementOrderDetailParam param) {
        return param.getOrderDetailId();
    }


    private Page<ProcurementOrderDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementOrderDetail getOldEntity(ProcurementOrderDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementOrderDetail getEntity(ProcurementOrderDetailParam param) {
        ProcurementOrderDetail entity = new ProcurementOrderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ProcurementOrderDetailResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        for (ProcurementOrderDetailResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            customerIds.add(datum.getCustomerId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<CustomerResult> results = customerService.getResults(customerIds);

        for (ProcurementOrderDetailResult datum : data) {

            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(datum.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(datum.getBrandId())) {
                    datum.setBrandResult(brandResult);
                    break;
                }
            }

            for (CustomerResult result : results) {
                if (datum.getCustomerId().equals(result.getCustomerId())) {
                    datum.setCustomerResult(result);
                    break;
                }
            }
        }
    }
}
