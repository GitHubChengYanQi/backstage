package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionJobBooking;
import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.mapper.ProductionJobBookingMapper;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingResult;
import cn.atsoft.dasheng.production.service.ProductionJobBookingDetailService;
import  cn.atsoft.dasheng.production.service.ProductionJobBookingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
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

    @Override
    public void add(ProductionJobBookingParam param){
        ProductionJobBooking entity = getEntity(param);
        /**
         * 查询所处任务
         * 取出任务数量等数据
         * 在下面做条件更新和拦截判断
         */
        ProductionTask productionTask = productionTaskService.getById(param.getProductionTaskId());
        Integer taskNumber = productionTask.getNumber();
        List<ProductionJobBooking> completeJob = this.query().eq("production_task_id", param.getProductionTaskId()).list();
        int completeNumber = 0 ;
        for (ProductionJobBooking productionJobBooking : completeJob) {
            completeNumber += productionJobBooking.getNumber();
        }
        /**
         * 如果报工数量与已经报工的数量 相加  等于任务中要求数量   则更新任务状态
         */
        if (completeNumber+param.getNumber()==taskNumber){
            productionTask.setStatus(99);
            productionTaskService.updateById(productionTask);
        }
        if (completeNumber+param.getNumber()>taskNumber){
            throw new ServiceException(500,"报工总数不可超过任务数量");
        }
        this.save(entity);
        /**
         * 保存子表信息
         */
        List<ProductionJobBookingDetail> jobBookingDetails = new ArrayList<>();
        List<ActivitiSetpSetDetail> setDetails = activitiSetpSetDetailService.query().eq("setps_id", entity.getStepsId()).eq("type","out").list();
        for (ActivitiSetpSetDetail setDetail : setDetails) {
            for (int i = 0; i < setDetail.getNum() * param.getNumber(); i++) {
                ProductionJobBookingDetail jobBookingDetail = new ProductionJobBookingDetail();
                jobBookingDetail.setJobBookingId(entity.getJobBookingId());
                jobBookingDetail.setSkuId(setDetail.getSkuId());
                jobBookingDetails.add(jobBookingDetail);
            }
        }
        jobBookingDetailService.saveBatch(jobBookingDetails);


    }

    @Override
    public void delete(ProductionJobBookingParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionJobBookingParam param){
        ProductionJobBooking oldEntity = getOldEntity(param);
        ProductionJobBooking newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionJobBookingResult findBySpec(ProductionJobBookingParam param){
        return null;
    }

    @Override
    public List<ProductionJobBookingResult> findListBySpec(ProductionJobBookingParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionJobBookingResult> findPageBySpec(ProductionJobBookingParam param){
        Page<ProductionJobBookingResult> pageContext = getPageContext();
        IPage<ProductionJobBookingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionJobBookingParam param){
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
