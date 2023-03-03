package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuHandleRecord;
import cn.atsoft.dasheng.erp.mapper.SkuHandleRecordMapper;
import cn.atsoft.dasheng.erp.model.params.SkuHandleRecordParam;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.SkuHandleRecordResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.SkuHandleRecordService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * sku 任务操作记录 服务实现类
 * </p>
 *
 * @author
 * @since 2022-10-25
 */
@Service
public class SkuHandleRecordServiceImpl extends ServiceImpl<SkuHandleRecordMapper, SkuHandleRecord> implements SkuHandleRecordService {

    @Autowired
    private ActivitiProcessTaskService taskService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StorehousePositionsService positionsService;

    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockDetailsService stockDetailsService;


    @Override
    public void add(SkuHandleRecordParam param) {
        SkuHandleRecord entity = getEntity(param);
        this.save(entity);
    }


    @Override
    public void addRecord(Long skuId, Long brandId, Long positionId, Long customerId, String source, ActivitiProcessTask task, Long operationNumber, Long nowStockNum, Long balanceNumber) {
        SkuHandleRecord skuHandleRecord = new SkuHandleRecord();
        skuHandleRecord.setSkuId(skuId);
        skuHandleRecord.setBrandId(brandId);
        skuHandleRecord.setPositionId(positionId);
        skuHandleRecord.setSource(source);
        skuHandleRecord.setNowStockNumber(nowStockNum);
        skuHandleRecord.setCustomerId(customerId);
        skuHandleRecord.setOperationNumber(operationNumber);
        skuHandleRecord.setBalanceNumber(balanceNumber);
        skuHandleRecord.setOperationTime(new Date());

        if (ToolUtil.isNotEmpty(task)) {
            skuHandleRecord.setSourceId(task.getProcessTaskId());
            skuHandleRecord.setReceiptId(task.getFormId());
            skuHandleRecord.setTaskId(task.getProcessTaskId());
            skuHandleRecord.setTheme(task.getTheme());
        }

        skuHandleRecord.setOperationUserId(LoginContextHolder.getContext().getUserId());
        this.save(skuHandleRecord);
    }

    @Override
    public void addRecord(Long skuId, Long brandId, Long positionId, Long customerId, Long taskId, Long number, String source) {
        Long beforeNumber = stockDetailsService.getNumberCountBySkuId(skuId);
        ActivitiProcessTask task = taskService.getById(taskId);
        SkuHandleRecord skuHandleRecord = new SkuHandleRecord();
        skuHandleRecord.setSkuId(skuId);
        skuHandleRecord.setBrandId(brandId);
        skuHandleRecord.setPositionId(positionId);

        skuHandleRecord.setSource(source);

        skuHandleRecord.setNowStockNumber(beforeNumber);
        skuHandleRecord.setCustomerId(customerId);
        skuHandleRecord.setOperationNumber(number);
        skuHandleRecord.setBalanceNumber(beforeNumber+number);
        skuHandleRecord.setOperationTime(new Date());

        if (ToolUtil.isNotEmpty(task)) {
            if (ToolUtil.isNotEmpty(task.getProcessTaskId())) {
                skuHandleRecord.setSourceId(task.getProcessTaskId());
            }
            if (ToolUtil.isNotEmpty(task.getFormId())) {
                skuHandleRecord.setReceiptId(task.getFormId());
            }
            if (ToolUtil.isNotEmpty(task.getProcessTaskId())) {
                skuHandleRecord.setTaskId(task.getProcessTaskId());
            }
            if (ToolUtil.isNotEmpty(task.getTheme())) {
                skuHandleRecord.setTheme(task.getTheme());
            }
        }
        skuHandleRecord.setOperationUserId(LoginContextHolder.getContext().getUserId());
        this.save(skuHandleRecord);
    }
    @Override
    public void addOutRecord(Long skuId, Long brandId, Long positionId, Long customerId, Long taskId, Long number, String source) {
        Long beforeNumber = stockDetailsService.getNumberCountBySkuId(skuId);
        ActivitiProcessTask task = taskService.getById(taskId);
        SkuHandleRecord skuHandleRecord = new SkuHandleRecord();
        skuHandleRecord.setSkuId(skuId);
        skuHandleRecord.setBrandId(brandId);
        skuHandleRecord.setPositionId(positionId);

        skuHandleRecord.setSource(source);

        skuHandleRecord.setNowStockNumber(beforeNumber);
        skuHandleRecord.setCustomerId(customerId);
        skuHandleRecord.setOperationNumber(number);
        skuHandleRecord.setBalanceNumber(beforeNumber-number);
        skuHandleRecord.setOperationTime(new Date());

        if (ToolUtil.isNotEmpty(task)) {
            if (ToolUtil.isNotEmpty(task.getProcessTaskId())) {
                skuHandleRecord.setSourceId(task.getProcessTaskId());
            }
            if (ToolUtil.isNotEmpty(task.getFormId())) {
                skuHandleRecord.setReceiptId(task.getFormId());
            }
            if (ToolUtil.isNotEmpty(task.getProcessTaskId())) {
                skuHandleRecord.setTaskId(task.getProcessTaskId());
            }
            if (ToolUtil.isNotEmpty(task.getTheme())) {
                skuHandleRecord.setTheme(task.getTheme());
            }
        }
        skuHandleRecord.setOperationUserId(LoginContextHolder.getContext().getUserId());
        this.save(skuHandleRecord);
    }
    @Override
    public void delete(SkuHandleRecordParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuHandleRecordParam param) {
        SkuHandleRecord oldEntity = getOldEntity(param);
        SkuHandleRecord newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuHandleRecordResult findBySpec(SkuHandleRecordParam param) {
        return null;
    }

    @Override
    public List<SkuHandleRecordResult> findListBySpec(SkuHandleRecordParam param) {
        return null;
    }

    @Override
    public PageInfo<SkuHandleRecordResult> findPageBySpec(SkuHandleRecordParam param) {
        if (ToolUtil.isNotEmpty(param.getPositionId())) {
            List<Long> list = positionsService.getEndChild(param.getPositionId());
            list.add(param.getPositionId());
            param.setPositionIds(list);
        }
        Page<SkuHandleRecordResult> pageContext = getPageContext();
        IPage<SkuHandleRecordResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuHandleRecordParam param) {
        return param.getRecordId();
    }

    private Page<SkuHandleRecordResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuHandleRecord getOldEntity(SkuHandleRecordParam param) {
        return this.getById(getKey(param));
    }

    private SkuHandleRecord getEntity(SkuHandleRecordParam param) {
        SkuHandleRecord entity = new SkuHandleRecord();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<SkuHandleRecordResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> instockOrderIds = new ArrayList<>();
        List<Long> taskIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        for (SkuHandleRecordResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            positionIds.add(datum.getPositionId());
            taskIds.add(datum.getTaskId());
            customerIds.add(datum.getCustomerId());
            userIds.add(datum.getOperationUserId());
            switch (datum.getSource()) {
                case "INSTOCK":
                    instockOrderIds.add(datum.getReceiptId());
                    break;
            }
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositionsResult> positionsServiceDetails = positionsService.getDetails(positionIds);
        List<InstockOrderResult> instockOrderResults = instockOrderService.getDetails(instockOrderIds);
        List<ActivitiProcessTask> tasks = taskIds.size() == 0 ? new ArrayList<>() : taskService.listByIds(taskIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<User> users= userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        brandResults.add(new BrandResult() {{
            setBrandId(0L);
            setBrandName("无品牌");
        }});

        for (SkuHandleRecordResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (ToolUtil.isNotEmpty(datum.getSkuId()) && datum.getSkuId().equals(skuResult.getSkuId())) {
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
            for (StorehousePositionsResult positionsServiceDetail : positionsServiceDetails) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && positionsServiceDetail.getStorehousePositionsId().equals(datum.getPositionId())) {
                    datum.setPositionsResult(positionsServiceDetail);
                    break;
                }
            }

            for (InstockOrderResult instockOrderResult : instockOrderResults) {
                if (ToolUtil.isNotEmpty(datum.getReceiptId()) && datum.getReceiptId().equals(instockOrderResult.getInstockOrderId())) {
                    datum.setInstockOrderResult(instockOrderResult);
                    break;
                }
            }

            for (ActivitiProcessTask task : tasks) {
                if ( ToolUtil.isNotEmpty(datum.getTaskId()) && task.getProcessTaskId().equals(datum.getTaskId())) {
                    datum.setTask(task);
                    break;
                }
            }
            for (CustomerResult customerResult : customerResults) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(customerResult.getCustomerId())) {
                    datum.setCustomerResult(customerResult);
                    break;
                }
            }
            for (User user : users) {
                if (ToolUtil.isNotEmpty(datum.getOperationUserId()) && datum.getOperationUserId().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }
            }
        }

    }

}
