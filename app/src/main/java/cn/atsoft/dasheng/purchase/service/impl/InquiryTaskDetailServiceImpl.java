package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.mapper.InquiryTaskDetailMapper;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskDetailResult;
import cn.atsoft.dasheng.purchase.service.InquiryTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 询价任务详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Service
public class InquiryTaskDetailServiceImpl extends ServiceImpl<InquiryTaskDetailMapper, InquiryTaskDetail> implements InquiryTaskDetailService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private BrandService brandService;

    @Override
    public void add(InquiryTaskDetailParam param) {
        InquiryTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InquiryTaskDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InquiryTaskDetailParam param) {
        InquiryTaskDetail oldEntity = getOldEntity(param);
        InquiryTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InquiryTaskDetailResult findBySpec(InquiryTaskDetailParam param) {
        return null;
    }

    @Override
    public List<InquiryTaskDetailResult> findListBySpec(InquiryTaskDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<InquiryTaskDetailResult> findPageBySpec(InquiryTaskDetailParam param) {
        Page<InquiryTaskDetailResult> pageContext = getPageContext();
        IPage<InquiryTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<InquiryTaskDetailResult> getDetails(Long taskId) {

        List<InquiryTaskDetail> taskDetails = this.query().eq("inquiry_task_id", taskId).list();
        List<InquiryTaskDetailResult> detailResults = new ArrayList<>();

        for (InquiryTaskDetail taskDetail : taskDetails) {
            InquiryTaskDetailResult detailResult = new InquiryTaskDetailResult();
            ToolUtil.copyProperties(taskDetail, detailResult);
            detailResults.add(detailResult);
        }


        return detailResults;
    }

    private Serializable getKey(InquiryTaskDetailParam param) {
        return param.getInquiryDetailId();
    }

    private Page<InquiryTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InquiryTaskDetail getOldEntity(InquiryTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private InquiryTaskDetail getEntity(InquiryTaskDetailParam param) {
        InquiryTaskDetail entity = new InquiryTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InquiryTaskDetailResult> data) {

    }

    /**
     * 通过父表主键查询
     *
     * @param inQuiruId
     * @return
     */
    @Override
    public List<InquiryTaskDetailResult> getDetailByInquiryId(Long inQuiruId) {
        if (ToolUtil.isEmpty(inQuiruId)) {
            throw new ServiceException(500, "请确定参数");
        }
        List<InquiryTaskDetail> taskDetails = this.lambdaQuery().eq(InquiryTaskDetail::getInquiryTaskId, inQuiruId).eq(InquiryTaskDetail::getDisplay, 1).list();
        List<InquiryTaskDetailResult> inquiryTaskDetailResults = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();

        for (InquiryTaskDetail taskDetail : taskDetails) {
            InquiryTaskDetailResult detailResult = new InquiryTaskDetailResult();
            ToolUtil.copyProperties(taskDetail, detailResult);
            inquiryTaskDetailResults.add(detailResult);
            skuIds.add(taskDetail.getSkuId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        for (InquiryTaskDetailResult inquiryTaskDetailResult : inquiryTaskDetailResults) {
            for (SkuResult skuResult : skuResults) {
                if (inquiryTaskDetailResult.getSkuId().equals(skuResult.getSkuId())) {
                    inquiryTaskDetailResult.setSkuResult(skuResult);
                    break;
                }
            }
        }

        //返回品牌详情
        List<Long> brandIds = new ArrayList<>();
        for (InquiryTaskDetail taskDetail : taskDetails) {
            brandIds.add(taskDetail.getBrandId());
        }
        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.query().in("brand_id", brandIds).list();
        for (InquiryTaskDetailResult inquiryTaskDetailResult : inquiryTaskDetailResults) {
            for (Brand brand : brands) {
                if (ToolUtil.isNotEmpty(inquiryTaskDetailResult.getBrandId()) && inquiryTaskDetailResult.getBrandId().equals(brand.getBrandId())) {
                    BrandResult brandResult =  new BrandResult();
                    ToolUtil.copyProperties(brand,brandResult);
                    inquiryTaskDetailResult.setBrandResult(brandResult);
                }
            }
        }
        return inquiryTaskDetailResults;

    }

    /**
     * 获取询价任务的sku
     *
     * @param taskId
     * @return
     */
    @Override
    public List<Long> getSku(Long taskId) {
        List<Long> skuIds = new ArrayList<>();
        List<InquiryTaskDetail> taskDetails = this.query().eq("inquiry_task_id", taskId).list();
        for (InquiryTaskDetail taskDetail : taskDetails) {
            skuIds.add(taskDetail.getSkuId());
        }
        return skuIds;
    }
}
