package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrderDetail;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.mapper.ProcurementOrderMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderResult;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderDetailService;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购单 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Service
public class ProcurementOrderServiceImpl extends ServiceImpl<ProcurementOrderMapper, ProcurementOrder> implements ProcurementOrderService {
    @Autowired
    private ProcurementOrderDetailService detailService;
    @Autowired
    private ProcurementPlanDetalService planDetailService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProcurementPlanService planService;

    @Override
    public void add(ProcurementOrderParam param) throws Exception {
        ProcurementOrder entity = getEntity(param);
        this.save(entity);


        ProcurementPlan procurementPlan = planService.getById(param.getProcurementPlanId());
        if (ToolUtil.isEmpty(procurementPlan)) {
            throw new ServiceException(500, "请确认采购计划");
        }

        List<ProcurementOrderDetailParam> params = param.getDetailParams();
        List<ProcurementOrderDetail> details = new ArrayList<>();
        Map<Long, List<ProcurementOrderDetailParam>> supplierMap = new HashMap<>();

        List<ProcurementPlanDetal> planDetals = new ArrayList<>();
        for (ProcurementOrderDetailParam procurementOrderDetailParam : params) {

            ProcurementPlanDetal planDetal = new ProcurementPlanDetal();//更改采购计划详情状态
            planDetal.setDetailId(procurementOrderDetailParam.getDetailId());
            planDetal.setStatus(procurementOrderDetailParam.getStatus());
            planDetals.add(planDetal);

            procurementOrderDetailParam.setProcurementOrderId(entity.getProcurementOrderId());
            ProcurementOrderDetail procurementOrderDetail = new ProcurementOrderDetail();
            ToolUtil.copyProperties(procurementOrderDetailParam, procurementOrderDetail);
            details.add(procurementOrderDetail);

            //过滤供应商
            List<ProcurementOrderDetailParam> detailParams = supplierMap.get(procurementOrderDetail.getCustomerId());
            if (ToolUtil.isEmpty(detailParams)) {
                List<ProcurementOrderDetailParam> detailParamList = new ArrayList<>();
                detailParamList.add(procurementOrderDetailParam);
                supplierMap.put(procurementOrderDetail.getCustomerId(), detailParamList);
            } else {
                detailParams.add(procurementOrderDetailParam);
                supplierMap.put(procurementOrderDetail.getCustomerId(), detailParams);
            }
        }
        planDetailService.updateBatchById(planDetals);
        planService.updateStatus(procurementPlan.getProcurementPlanId());

        //添加合同
        List<Contract> contracts = new ArrayList<>();
        for (Map.Entry<Long, List<ProcurementOrderDetailParam>> longListEntry : supplierMap.entrySet()) {
            Contract contract = new Contract();
            contract.setName("供应商id:" + longListEntry.getKey());
            contracts.add(contract);
        }
        contractService.saveBatch(contracts);
        detailService.saveBatch(details);
    }

    @Override
    public void delete(ProcurementOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProcurementOrderParam param) {
        ProcurementOrder oldEntity = getOldEntity(param);
        ProcurementOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProcurementOrderResult findBySpec(ProcurementOrderParam param) {
        return null;
    }

    @Override
    public List<ProcurementOrderResult> findListBySpec(ProcurementOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcurementOrderResult> findPageBySpec(ProcurementOrderParam param) {
        Page<ProcurementOrderResult> pageContext = getPageContext();
        IPage<ProcurementOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProcurementOrderParam param) {
        return param.getProcurementOrderId();
    }

    private Page<ProcurementOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementOrder getOldEntity(ProcurementOrderParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementOrder getEntity(ProcurementOrderParam param) {
        ProcurementOrder entity = new ProcurementOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
