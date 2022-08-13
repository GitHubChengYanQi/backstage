package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.InstockReceiptMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.InstockReceiptParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.InstockReceiptService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 入库记录单 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
@Service
public class InstockReceiptServiceImpl extends ServiceImpl<InstockReceiptMapper, InstockReceipt> implements InstockReceiptService {

    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private ActivitiProcessTaskService taskService;

    @Override
    public void add(InstockReceiptParam param) {
        InstockReceipt entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 入库单
     *
     * @param param
     * @param instockLogDetails
     */
    @Override
    public void addReceipt(InstockOrderParam param, List<InstockLogDetail> instockLogDetails) {

        InstockReceipt entity = new InstockReceipt();
        CodingRules codingRules = codingRulesService.query().eq("module", "1").eq("state", 1).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            entity.setCoding(coding);
        }
        entity.setInstockOrderId(param.getInstockOrderId());
        this.save(entity);

        for (InstockLogDetail instockLogDetail : instockLogDetails) {
            instockLogDetail.setReceiptId(entity.getReceiptId());
        }

    }

    @Override
    public InstockReceiptResult detail(Long receiptId) { 
        InstockReceipt instockReceipt = this.getById(receiptId);
        InstockReceiptResult result = new InstockReceiptResult();
        ToolUtil.copyProperties(instockReceipt, result);

        List<InstockLogDetail> instockLogDetails = instockLogDetailService.query().eq("receipt_id", result.getReceiptId()).eq("display", 1).list();
        List<InstockLogDetailResult> detailResults = BeanUtil.copyToList(instockLogDetails, InstockLogDetailResult.class);
        instockLogDetailService.format(detailResults);
        Map<Long, List<InstockLogDetailResult>> map = new HashMap<>();
        Map<String, List<InstockLogDetailResult>> customerMap = new HashMap<>();

        for (InstockLogDetailResult detailResult : detailResults) {
            List<InstockLogDetailResult> results = map.get(detailResult.getCustomerId());
            if (ToolUtil.isEmpty(results)) {
                results = new ArrayList<>();
            }
            results.add(detailResult);
            map.put(detailResult.getCustomerId(), results);
        }

        for (Long customerId : map.keySet()) {
            List<InstockLogDetailResult> results = map.get(customerId);
            Customer customer = customerService.getById(customerId);
            customerMap.put(customer.getCustomerName(), results);
        }

        if (ToolUtil.isNotEmpty(result.getCreateUser())) {
            User user = userService.getById(result.getCreateUser());
            result.setUser(user);
        }

        if (ToolUtil.isNotEmpty(result.getInstockOrderId())) {
            String source;
            InstockOrder instockOrder = instockOrderService.getById(result.getInstockOrderId());
            ActivitiProcessTask task = taskService.getByFormId(instockOrder.getInstockOrderId());
            source = task.getTaskName() + "/" + instockOrder.getCoding();
            result.setSource(source);
        }


        result.setCustomerMap(customerMap);
        return result;
    }


    @Override
    public void delete(InstockReceiptParam param) {
        this.removeById(getKey(param));
    }


    @Override
    public void update(InstockReceiptParam param) {
        InstockReceipt oldEntity = getOldEntity(param);
        InstockReceipt newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockReceiptResult findBySpec(InstockReceiptParam param) {
        return null;
    }

    @Override
    public List<InstockReceiptResult> findListBySpec(InstockReceiptParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockReceiptResult> findPageBySpec(InstockReceiptParam param) {
        Page<InstockReceiptResult> pageContext = getPageContext();
        IPage<InstockReceiptResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockReceiptParam param) {
        return param.getReceiptId();
    }

    private Page<InstockReceiptResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockReceipt getOldEntity(InstockReceiptParam param) {
        return this.getById(getKey(param));
    }

    private InstockReceipt getEntity(InstockReceiptParam param) {
        InstockReceipt entity = new InstockReceipt();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
