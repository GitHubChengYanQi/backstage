package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionJobBooking;
import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.mapper.ProductionJobBookingMapper;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
import cn.atsoft.dasheng.production.service.ProductionJobBookingDetailService;
import cn.atsoft.dasheng.production.service.ProductionJobBookingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionTaskDetailService;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报工表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
@Service
public class ProductionJobBookingServiceImpl extends ServiceImpl<ProductionJobBookingMapper, ProductionJobBooking> implements ProductionJobBookingService {

    @Autowired
    private ProductionTaskService productionTaskService;

    @Autowired
    private ActivitiSetpSetService activitiSetpSetService;

    @Autowired
    private ActivitiSetpSetDetailService activitiSetpSetDetailService;

    @Autowired
    private ProductionJobBookingDetailService jobBookingDetailService;

    @Autowired
    private ProductionTaskDetailService productionTaskDetailService;

    @Autowired
    private InkindService inkindService;

    @Override
    public void add(ProductionJobBookingParam param) {
        ProductionJobBooking entity = getEntity(param);
        entity.setSource("productionTask");
        entity.setSourceId(param.getProductionTaskId());
        this.save(entity);
        /**
         * 查询所处任务
         * 取出任务数量等数据
         * 在下面做条件更新和拦截判断
         */
        /**
         * 查询出已经报工的数量
         */

        ProductionTask productionTask = productionTaskService.getById(param.getProductionTaskId());
        if (ToolUtil.isEmpty(productionTask)){
            throw new ServiceException(500,"未查询到此任务");
        }
        List<ProductionTaskDetailResult> taskDetailResults = productionTaskDetailService.resultsByTaskId(productionTask.getProductionTaskId());
        List<ProductionJobBooking> jobBookings = this.query().eq("production_task_id", productionTask.getProductionTaskId()).list();
        List<Long> jobBookingIds = new ArrayList<>();
        for (ProductionJobBooking jobBooking : jobBookings) {
            jobBookingIds.add(jobBooking.getJobBookingId());
        }
        List<ProductionJobBookingDetail> jobBookingDetails = jobBookingIds.size() == 0 ? new ArrayList<>() : jobBookingDetailService.query().in("job_booking_id", jobBookingIds).list();

        List<ProductionJobBookingDetailResult> jobBookingDetailResults = new ArrayList<>();
        for (ProductionJobBookingDetail jobBookingDetail : jobBookingDetails) {
            ProductionJobBookingDetailResult jobBookingDetailResult = new ProductionJobBookingDetailResult();
            ToolUtil.copyProperties(jobBookingDetail, jobBookingDetailResult);
            jobBookingDetailResult.setNumber(1);
            jobBookingDetailResults.add(jobBookingDetailResult);
        }
        // 表示name为key，接着如果有重复的，那么从Pool对象o1与o2中筛选出一个，这里选择o1，
        // 并把name重复，需要将value与o1进行合并的o2, 赋值给o1，最后返回o1
        /**
         * 把已经报工的数量处理出来
         */
        jobBookingDetailResults = jobBookingDetailResults.stream().collect(Collectors.toMap(ProductionJobBookingDetailResult::getSkuId, a -> a, (o1, o2) -> {
            o1.setNumber(o1.getNumber() + o2.getNumber());
            return o1;
        })).values().stream().collect(Collectors.toList());

        /**
         * 把已经报工的数量与提交报工数量相加
         */
        for (ProductionJobBookingDetailResult jobBookingDetailResult : jobBookingDetailResults) {
            for (ProductionJobBookingDetailParam detailParam : param.getDetailParams()) {
                if (jobBookingDetailResult.getSkuId().equals(detailParam.getSkuId())){
                    jobBookingDetailResult.setNumber(jobBookingDetailResult.getNumber()+detailParam.getNumber());
                }
            }
        }
        List<Boolean> booleans = new ArrayList<>();
        for (ProductionTaskDetailResult detailResult : taskDetailResults) {
            for (ProductionJobBookingDetailResult jobBookingDetailResult : jobBookingDetailResults) {
                if (detailResult.getOutSkuId().equals(jobBookingDetailResult.getSkuId()) && detailResult.getNumber()<jobBookingDetailResult.getNumber()){
                    throw new ServiceException(500,"报工物料的总数量不得超过任务中物料数量");
                }
                if (detailResult.getOutSkuId().equals(jobBookingDetailResult.getSkuId()) && detailResult.getNumber() == jobBookingDetailResult.getNumber()){
                    booleans.add(true);
                }
            }
        }
        if (booleans.size() == taskDetailResults.size()){
            //TODO 更新任务状态为完成
            productionTask.setStatus(99);
            productionTaskService.updateById(productionTask);
        }
        /**
         * 保存子表信息
         */

        List<Inkind> inkinds = new ArrayList<>();
        for (ProductionJobBookingDetailParam detailParam : param.getDetailParams()) {
            for (Integer i = 0; i < detailParam.getNumber(); i++) {

                Inkind inkind = new Inkind();
                inkind.setSource("jobBooking");
                inkind.setSourceId(entity.getSourceId());
                inkind.setSkuId(detailParam.getSkuId());;
                inkind.setNumber(1L);
                inkinds.add(inkind);
            }
        }

        inkindService.saveBatch(inkinds);
        List<ProductionJobBookingDetail> jobBookingDetailsEntity = new ArrayList<>();
        for (Inkind inkind : inkinds) {
        ProductionJobBookingDetail jobBookingdetail = new ProductionJobBookingDetail();
            jobBookingdetail.setSkuId(inkind.getSkuId());
            jobBookingdetail.setInkindId(inkind.getInkindId());
            jobBookingdetail.setJobBookingId(entity.getJobBookingId());
            jobBookingdetail.setSource("productionTask");
            jobBookingdetail.setSourceId(productionTask.getProductionTaskId());
            jobBookingDetailsEntity.add(jobBookingdetail);
        }


        jobBookingDetailService.saveBatch(jobBookingDetailsEntity);







    }

    @Override
    public void delete(ProductionJobBookingParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionJobBookingParam param) {
        ProductionJobBooking oldEntity = getOldEntity(param);
        ProductionJobBooking newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionJobBookingResult findBySpec(ProductionJobBookingParam param) {
        return null;
    }

    @Override
    public List<ProductionJobBookingResult> findListBySpec(ProductionJobBookingParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionJobBookingResult> findPageBySpec(ProductionJobBookingParam param) {
        Page<ProductionJobBookingResult> pageContext = getPageContext();
        IPage<ProductionJobBookingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionJobBookingParam param) {
        return param.getJobBookingId();
    }

    private Page<ProductionJobBookingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionJobBooking getOldEntity(ProductionJobBookingParam param) {
        return this.getById(getKey(param));
    }

    private ProductionJobBooking getEntity(ProductionJobBookingParam param) {
        ProductionJobBooking entity = new ProductionJobBooking();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
