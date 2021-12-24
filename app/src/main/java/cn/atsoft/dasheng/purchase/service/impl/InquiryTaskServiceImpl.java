package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.mapper.InquiryTaskMapper;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskParam;

import cn.atsoft.dasheng.purchase.model.result.InquiryTaskResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;

import cn.atsoft.dasheng.purchase.service.InquiryTaskDetailService;
import cn.atsoft.dasheng.purchase.service.InquiryTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
 * 询价任务 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Service
public class InquiryTaskServiceImpl extends ServiceImpl<InquiryTaskMapper, InquiryTask> implements InquiryTaskService {
    @Autowired
    private InquiryTaskDetailService detailService;
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseQuotationService quotationService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SupplyService supplyService;

    @Override
    public void add(InquiryTaskParam param) {
        InquiryTask entity = getEntity(param);
        this.save(entity);

        List<Long> skuIds = new ArrayList<>();
        for (InquiryTaskDetailParam detailParam : param.getDetailParams()) {
            skuIds.add(detailParam.getSkuId());
        }
        long count = skuIds.stream().distinct().count();
        if (param.getDetailParams().size() != count) {
            throw new ServiceException(500, "请不要添加重复物料");
        }

        List<InquiryTaskDetail> details = new ArrayList<>();
        for (InquiryTaskDetailParam detailParam : param.getDetailParams()) {
            InquiryTaskDetail taskDetail = new InquiryTaskDetail();
            ToolUtil.copyProperties(detailParam, taskDetail);
            taskDetail.setInquiryTaskId(entity.getInquiryTaskId());
            details.add(taskDetail);
        }
        detailService.saveBatch(details);
    }

    @Override
    public void delete(InquiryTaskParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InquiryTaskParam param) {
        InquiryTask oldEntity = getOldEntity(param);
        InquiryTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InquiryTaskResult findBySpec(InquiryTaskParam param) {
        return null;
    }

    @Override
    public List<InquiryTaskResult> findListBySpec(InquiryTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<InquiryTaskResult> findPageBySpec(InquiryTaskParam param) {
        Page<InquiryTaskResult> pageContext = getPageContext();
        IPage<InquiryTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InquiryTaskParam param) {
        return param.getInquiryTaskId();
    }

    private Page<InquiryTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InquiryTask getOldEntity(InquiryTaskParam param) {
        return this.getById(getKey(param));
    }

    private InquiryTask getEntity(InquiryTaskParam param) {
        InquiryTask entity = new InquiryTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InquiryTaskResult> data) {
        List<Long> userIds = new ArrayList<>();

        for (InquiryTaskResult datum : data) {
            userIds.add(datum.getCreateUser());
        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (InquiryTaskResult datum : data) {
            for (User user : users) {
                if (datum.getUserId().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }
            }
        }

    }

    @Override
    public InquiryTaskResult detail(Long taskId) {

        InquiryTask inquiryTask = this.getById(taskId);
        InquiryTaskResult taskResult = new InquiryTaskResult();
        ToolUtil.copyProperties(inquiryTask, taskResult);

        List<PurchaseQuotationResult> bySource = quotationService.getListBySource(taskResult.getInquiryTaskId());
        taskResult.setQuotationResults(bySource);

        List<Long> sku = detailService.getSku(taskResult.getInquiryTaskId());

        //返回供应商
        List<CustomerResult> suppliers = supplyService.getSupplyBySku(sku);
        taskResult.setCustomerResults(suppliers);



        return taskResult;
    }
}
