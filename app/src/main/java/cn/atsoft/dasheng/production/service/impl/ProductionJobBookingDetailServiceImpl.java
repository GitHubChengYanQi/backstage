package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.mapper.ProductionJobBookingDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingDetailParam;
import cn.atsoft.dasheng.production.model.request.JobBookingDetailCount;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import  cn.atsoft.dasheng.production.service.ProductionJobBookingDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.model.request.ProcurementDetailSkuTotal;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 报工详情表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
@Service
public class ProductionJobBookingDetailServiceImpl extends ServiceImpl<ProductionJobBookingDetailMapper, ProductionJobBookingDetail> implements ProductionJobBookingDetailService {

    @Override
    public void add(ProductionJobBookingDetailParam param){
        ProductionJobBookingDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionJobBookingDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionJobBookingDetailParam param){
        ProductionJobBookingDetail oldEntity = getOldEntity(param);
        ProductionJobBookingDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionJobBookingDetailResult findBySpec(ProductionJobBookingDetailParam param){
        return null;
    }

    @Override
    public List<ProductionJobBookingDetailResult> findListBySpec(ProductionJobBookingDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionJobBookingDetailResult> findPageBySpec(ProductionJobBookingDetailParam param){
        Page<ProductionJobBookingDetailResult> pageContext = getPageContext();
        IPage<ProductionJobBookingDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionJobBookingDetailParam param){
        return param.getJobBookingDetailId();
    }

    private Page<ProductionJobBookingDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionJobBookingDetail getOldEntity(ProductionJobBookingDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionJobBookingDetail getEntity(ProductionJobBookingDetailParam param) {
        ProductionJobBookingDetail entity = new ProductionJobBookingDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<ProductionJobBookingDetailResult> resultsByJobBookingIds(List<Long> jobBookingIds){
        if (ToolUtil.isEmpty(jobBookingIds) || jobBookingIds.size() == 0){
            return new ArrayList<>();
        }
        List<ProductionJobBookingDetail> details = this.query().in("job_booking_id", jobBookingIds).list();
        List<ProductionJobBookingDetailResult> results = new ArrayList<>();
        for (ProductionJobBookingDetail detail : details) {
            ProductionJobBookingDetailResult result = new ProductionJobBookingDetailResult();
            ToolUtil.copyProperties(detail,result);
            results.add(result);
        }
        return results;
    }
    @Override
    public List<JobBookingDetailCount> resultsByProductionTaskIds(List<Long> productionTaskIds){
        if (ToolUtil.isEmpty(productionTaskIds) || productionTaskIds.size() == 0){
            return new ArrayList<>();
        }
        List<ProductionJobBookingDetail> details = this.query().in("source_id", productionTaskIds).eq("source","productionTask").list();
        List<ProductionJobBookingDetailResult> results = new ArrayList<>();
        List<JobBookingDetailCount> counts = new ArrayList<>();
        for (ProductionJobBookingDetail detail : details) {
            JobBookingDetailCount jobBookingDetailCount = new JobBookingDetailCount();
            jobBookingDetailCount.setNumber(1);
            jobBookingDetailCount.setSource(detail.getSource());
            jobBookingDetailCount.setSourceId(detail.getSourceId());
            jobBookingDetailCount.setSkuId(detail.getSkuId());
            counts.add(jobBookingDetailCount);
            ProductionJobBookingDetailResult result = new ProductionJobBookingDetailResult();
            ToolUtil.copyProperties(detail,result);
            results.add(result);
        }
//        results = results.stream().collect(Collectors.toMap(ProductionJobBookingDetailResult::getSkuId && , a -> a, (o1, o2) -> {
//            o1.setNumber(o1.getNumber() + o2.getNumber());
//            return o1;
//        })).values().stream().collect(Collectors.toList());
        List<JobBookingDetailCount> totalList = new ArrayList<>();
        counts.parallelStream().collect(Collectors.groupingBy(detail->detail.getSkuId()+'_'+detail.getSource()+"_"+detail.getSourceId(),Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> {
                        return new JobBookingDetailCount(a.getSkuId(), a.getSource(), a.getSourceId(), a.getNumber() + b.getNumber());
                    }).ifPresent(totalList::add);
                }
        );

        return totalList;
    }

}
