package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.ProductOrderDetailsMapper;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuValuesRequest;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.*;

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
    @Autowired
    private SkuService skuService;
    @Autowired
    private AttributeValuesServiceImpl attributeValuesService;
    @Autowired
    private ItemAttributeService itemAttributeService;

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
        List<Long> skuIds = new ArrayList<>();
        for (ProductOrderDetailsResult datum : data) {
            orderIds.add(datum.getProductOrderId());
            spuIds.add(datum.getSpuId());
            skuIds.add(datum.getSkuId());
        }

        List<ProductOrder> productOrders = orderIds.size() == 0 ? new ArrayList<>() : productOrderService.lambdaQuery()
                .in(ProductOrder::getProductOrderId, orderIds)
                .list();

        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery()
                .in(Spu::getSpuId, spuIds)
                .list();

        List<Sku> skus = skuService.lambdaQuery().in(Sku::getSkuId, skuIds)
                .list();
        //通过map取出对用的sku  获取json数据
        Map<Long, String> map = new HashMap<>();
        for (ProductOrderDetailsResult datum : data) {
            for (Sku sku : skus) {
                if (datum.getSkuId() != null && datum.getSkuId().equals(sku.getSkuId())) {
                    map.put(datum.getSkuId(), sku.getSkuName());
                }
            }
        }


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
//            String s = map.get(datum.getSkuId());
////            SkuJson skuJson = JSON.parseObject(s, SkuJson.class);
//            JSONArray jsonArray = JSONUtil.parseArray(s);
//            List<SkuValuesRequest> valuesRequests = JSONUtil.toList(jsonArray, SkuValuesRequest.class);
//            List<Long> attributeValueIds = new ArrayList<>();
//            for (SkuValuesRequest valuesRequest : valuesRequests) {
//                attributeValueIds.add(valuesRequest.getAttributeValueId());
//            }
//            List<AttributeValues> valuesList = attributeValueIds.size() == 0 ? new ArrayList<>() :
//                    attributeValuesService.query().in("attribute_values_id", attributeValueIds).list();
//            List<Long> attributeId = new ArrayList<>();
//            for (AttributeValues attributeValues : valuesList) {
//                attributeId.add(attributeValues.getAttributeId());
//            }
//            List<ItemAttribute> itemAttributes = attributeId.size() == 0 ? new ArrayList<>() :
//                    itemAttributeService.query().in("attribute_id", attributeId).list();

//            List<AttributeValuesResult> attributeValuesResults = new ArrayList<>();
//            for (AttributeValues attributeValues : valuesList) {
//                AttributeValuesResult attributeValuesResult = new AttributeValuesResult();
//                ToolUtil.copyProperties(attributeValues, attributeValuesResult);
//                attributeValuesResults.add(attributeValuesResult);
//            }
//            for (AttributeValuesResult attributeValuesResult : attributeValuesResults) {
//                for (ItemAttribute itemAttribute : itemAttributes) {
//                    if (attributeValuesResult.getAttributeId().equals(itemAttribute.getAttributeId())) {
//                        ItemAttributeResult attributeResult = new ItemAttributeResult();
//                        ToolUtil.copyProperties(itemAttribute, attributeResult);
//                        attributeValuesResult.setItemAttributeResult(attributeResult);
//                    }
//                }
//            }

//            datum.setAttributeValuesResults(attributeValuesResults);

            List<SkuValuesRequest> list = new ArrayList<>();
            datum.setSkuValuesRequests(list);
        }
    }

    public static List<Long> pastLeep(List<String> list) {
        List<Long> newList = new ArrayList<>();
        Set set = new HashSet();
        for (String str : list) {
            if (set.add(str)) {
                newList.add(Long.valueOf(str));
            }
        }
        return newList;
    }
}
