package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.mapper.AnomalyMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.AnomalyDetailService;
import cn.atsoft.dasheng.erp.service.AnomalyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.SkuService;
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
 * @since 2022-05-27
 */
@Service
public class AnomalyServiceImpl extends ServiceImpl<AnomalyMapper, Anomaly> implements AnomalyService {

    @Autowired
    private InkindService inkindService;
    @Autowired
    private AnomalyDetailService detailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;

    @Override
    public void add(AnomalyParam param) {
        Anomaly entity = getEntity(param);
        this.save(entity);

        List<AnomalyDetail> anomalyDetails = new ArrayList<>();
        for (AnomalyDetailParam detailParam : param.getDetailParams()) {
            //创建实物
            Inkind inkind = new Inkind();
            inkind.setSkuId(param.getSkuId());
            inkind.setBrandId(param.getBrandId());
            inkind.setCustomerId(param.getCustomerId());
            inkind.setNumber(1L);
            inkind.setSource("入库异常");
            inkindService.save(inkind);
            detailParam.setInkindId(inkind.getInkindId());

            AnomalyDetail anomalyDetail = new AnomalyDetail();
            ToolUtil.copyProperties(detailParam, anomalyDetail);
            anomalyDetails.add(anomalyDetail);
        }
        detailService.saveBatch(anomalyDetails);
    }

    @Override
    public void delete(AnomalyParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AnomalyParam param) {
        Anomaly oldEntity = getOldEntity(param);
        Anomaly newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AnomalyResult findBySpec(AnomalyParam param) {
        return null;
    }

    @Override
    public List<AnomalyResult> findListBySpec(AnomalyParam param) {
        return null;
    }

    @Override
    public PageInfo<AnomalyResult> findPageBySpec(AnomalyParam param) {
        Page<AnomalyResult> pageContext = getPageContext();
        IPage<AnomalyResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyParam param) {
        return param.getAnomalyId();
    }

    private Page<AnomalyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Anomaly getOldEntity(AnomalyParam param) {
        return this.getById(getKey(param));
    }

    private Anomaly getEntity(AnomalyParam param) {
        Anomaly entity = new Anomaly();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<AnomalyResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();

        for (AnomalyResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            customerIds.add(datum.getCustomerId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);

        for (AnomalyResult datum : data) {

            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(datum.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brandResult.getBrandId())) {
                    datum.setBrandResult(brandResult);
                    break;
                }
            }

            for (CustomerResult customerResult : customerResults) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(customerResult.getCustomerId())) {
                    datum.setCustomerResult(customerResult);
                    break;
                }
            }

        }
    }
}
