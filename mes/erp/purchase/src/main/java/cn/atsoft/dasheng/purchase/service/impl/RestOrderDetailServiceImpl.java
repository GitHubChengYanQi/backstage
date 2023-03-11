package cn.atsoft.dasheng.purchase.service.impl;



import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.entity.RestOrder;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;

import cn.atsoft.dasheng.goods.sku.model.result.SkuListResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.purchase.entity.RestOrderDetail;
import cn.atsoft.dasheng.purchase.mapper.RestOrderDetailMapper;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailHistory;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.params.ViewParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderDetailResult;
import cn.atsoft.dasheng.purchase.model.result.ViewResult;
import cn.atsoft.dasheng.purchase.service.RestOrderDetailService;
import cn.atsoft.dasheng.service.IErpBase;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service("RestOrderDetailService")
public class RestOrderDetailServiceImpl extends ServiceImpl<RestOrderDetailMapper, RestOrderDetail> implements RestOrderDetailService, IErpBase {

    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private RestOrderDetailService detailService;
    @Autowired
    private RestSkuService skuService;


    @Override
    public void add(RestOrderDetailParam param) {
        RestOrderDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RestOrderDetailParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));
    }

    @Override
    public void update(RestOrderDetailParam param) {
        RestOrderDetail oldEntity = getOldEntity(param);
        RestOrderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestOrderDetailResult findBySpec(RestOrderDetailParam param) {
        return null;
    }

    @Override
    public List<RestOrderDetailResult> findListBySpec(RestOrderDetailParam param) {
        List<RestOrderDetailResult> page = this.baseMapper.customList( param);
        format(page);
        return page;
    }
    @Override
    public List<RestOrderDetailResult> historyList(RestOrderDetailParam param) {
        List<RestOrderDetailResult> list = this.baseMapper.historyList( param);
        List<RestOrderDetailResult> result = new ArrayList<>();
        for (RestOrderDetailHistory history : param.getHistoryParam()) {
            for (RestOrderDetailResult detailResult : list) {
                if (history.getSkuId().equals(detailResult.getSkuId()) && history.getCustomerId().equals(detailResult.getCustomerId())) {
                    if (ToolUtil.isEmpty(history.getBrandId()) || history.getBrandId().equals(detailResult.getBrandId())){
                        result.add(detailResult);
                        break;
                    }
                }
            }
        }
        format(result);
        return result;
    }
    @Override
    public List<ViewResult> viewOrderDetail(ViewParam param) {
        List<ViewResult> list = this.baseMapper.viewOrderDetail( param);

//        format(result);
        return list;
    }
    @Override
    public ViewResult view(ViewParam param) {

//        format(result);
        return this.baseMapper.view( param);
    }
    @Override
    public ViewResult orderView(ViewParam param) {

//        format(result);
        return this.baseMapper.orderView( param);
    }

    @Override
    public PageInfo<RestOrderDetailResult> findPageBySpec(RestOrderDetailParam param) {
        Page<RestOrderDetailResult> pageContext = getPageContext();
        IPage<RestOrderDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void  format(List<RestOrderDetailResult> param) {
        List<Long> skuIds = param.stream().map(RestOrderDetailResult::getSkuId).distinct().collect(Collectors.toList());
        List<SkuListResult> restSkuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.viewResultsByIds(skuIds);
        for (RestOrderDetailResult datum : param) {
            for (SkuListResult restSkuResult : restSkuResults) {
                if (datum.getSkuId().equals(restSkuResult.getSkuId())){
                    datum.setSkuResult(restSkuResult);
                    break;
                }
            }
        }

    }

    private Serializable getKey(RestOrderDetailParam param) {
        return param.getDetailId();
    }

    private Page<RestOrderDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestOrderDetail getOldEntity(RestOrderDetailParam param) {
        return this.getById(getKey(param));
    }

    private RestOrderDetail getEntity(RestOrderDetailParam param) {
        RestOrderDetail entity = new RestOrderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


    @Override
    public PageInfo getOrderList(Map<String, Object> param) {
        return null;
    }

    @Override
    public List<cn.atsoft.dasheng.model.result.RestOrderDetailResult> getOrderDetailList(Map<String, Object> param) {
        RestOrderDetailParam restOrderDetailParam = BeanUtil.mapToBean(param, RestOrderDetailParam.class, true);
        List<RestOrderDetailResult> pageBySpec = this.findListBySpec(restOrderDetailParam);
        return BeanUtil.copyToList(pageBySpec,cn.atsoft.dasheng.model.result.RestOrderDetailResult.class);
    }
    @Override
    public List<cn.atsoft.dasheng.entity.RestOrderDetail> getDetailListByOrderId(Long orderId) {
        List<RestOrderDetail> list = this.lambdaQuery().eq(RestOrderDetail::getOrderId, orderId).eq(RestOrderDetail::getDisplay, 1).list();
        return BeanUtil.copyToList(list, cn.atsoft.dasheng.entity.RestOrderDetail.class);
    }

    @Override
    public List<cn.atsoft.dasheng.entity.RestOrderDetail> getDetailListByOrderDetailIds(List<Long> detailIds) {
        if (ToolUtil.isEmpty(detailIds) || detailIds.size() == 0){
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(this.listByIds(detailIds), cn.atsoft.dasheng.entity.RestOrderDetail.class);
    }

    @Override
    public void updateDetailList(List<cn.atsoft.dasheng.entity.RestOrderDetail> dataList) {
        List<RestOrderDetail> restOrderDetails = BeanUtil.copyToList(dataList, RestOrderDetail.class);
        this.updateBatchById(restOrderDetails);
    }

    @Override
    public RestOrder getOrderById(Long orderId) {
        return null;
    }


}
