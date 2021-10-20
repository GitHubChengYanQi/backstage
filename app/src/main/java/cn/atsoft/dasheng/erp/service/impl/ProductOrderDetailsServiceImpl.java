package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.mapper.ProductOrderDetailsMapper;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderDetailsResult;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ProductOrderService;
import cn.atsoft.dasheng.erp.service.SpuService;
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
 * 产品订单详情 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
@Service
public class ProductOrderDetailsServiceImpl extends ServiceImpl<ProductOrderDetailsMapper, ProductOrderDetails> implements ProductOrderDetailsService {

    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private SpuService spuService;

    @Override
    public void add(ProductOrderDetailsParam param) {
        ProductOrderDetails entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductOrderDetailsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductOrderDetailsParam param) {
        ProductOrderDetails oldEntity = getOldEntity(param);
        ProductOrderDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductOrderDetailsResult findBySpec(ProductOrderDetailsParam param) {
        return null;
    }

    @Override
    public List<ProductOrderDetailsResult> findListBySpec(ProductOrderDetailsParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductOrderDetailsResult> findPageBySpec(ProductOrderDetailsParam param) {
        Page<ProductOrderDetailsResult> pageContext = getPageContext();
        IPage<ProductOrderDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductOrderDetailsParam param) {
        return param.getProductOrderDetailsId();
    }

    private Page<ProductOrderDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductOrderDetails getOldEntity(ProductOrderDetailsParam param) {
        return this.getById(getKey(param));
    }

    private ProductOrderDetails getEntity(ProductOrderDetailsParam param) {
        ProductOrderDetails entity = new ProductOrderDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ProductOrderDetailsResult> data) {
        List<Long> orderIds = new ArrayList<>();
        List<Long> spuIds = new ArrayList<>();
        for (ProductOrderDetailsResult datum : data) {
            orderIds.add(datum.getProductOrderId());
            spuIds.add(datum.getSpuId());
        }

        List<ProductOrder> productOrders = orderIds.size() == 0 ? new ArrayList<>() : productOrderService.lambdaQuery()
                .in(ProductOrder::getProductOrderId, orderIds)
                .list();

        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuId, spuIds).list();

        for (ProductOrderDetailsResult datum : data) {
            //返回产品订单
            for (ProductOrder productOrder : productOrders) {
                if (datum.getProductOrderId() != null && datum.getProductOrderId().equals(productOrder.getProductOrderId())) {
                    ProductOrderResult productOrderResult = new ProductOrderResult();
                    ToolUtil.copyProperties(productOrder, productOrderResult);
                    datum.setProductOrderResult(productOrderResult);
                    break;
                }
            }
            //返回spu
            for (Spu spu : spus) {
                if (datum.getSpuId() != null && datum.getSpuId().equals(spu.getSpuId())) {
                    SpuResult spuResult = new SpuResult();
                    ToolUtil.copyProperties(spu, spuResult);
                    datum.setSpuResult(spuResult);
                    break;
                }
            }
        }
    }
}
