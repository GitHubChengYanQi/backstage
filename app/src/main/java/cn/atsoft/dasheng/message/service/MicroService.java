package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.service.ProductionCardService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MicroService {


    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private ProductionWorkOrderService productionWorkOrderService;

    @Autowired
    private ProductionCardService productionCardService;


    public void microServiceDo(MicroServiceEntity microServiceEntity) {
        switch (microServiceEntity.getType()) {
            case CONTRACT:

                switch (microServiceEntity.getOperationType()) {
                    case SAVE:
                        //保存

                        break;
                    case ADD:
                        ContractParam contractParam = JSON.parseObject(microServiceEntity.getObject().toString(), ContractParam.class);
                        break;
                }
                break;

            case PRODUCTION_PLAN:
                ProductionPlanParam productionPlanParam = JSON.parseObject(microServiceEntity.getObject().toString(), ProductionPlanParam.class);
                switch (microServiceEntity.getOperationType()) {
                    case ADD:
                        productionPlanService.add(productionPlanParam);
                        break;
                    case SAVE:
                        //保存
                        ProductionPlan productionPlan = new ProductionPlan();
                        ToolUtil.copyProperties(productionPlanParam, productionPlan);
                        productionPlanService.save(productionPlan);
                        break;
                    case UPDATE:
                        productionPlanService.update(productionPlanParam);
                        break;
                    case DELETE:
                        productionPlanService.delete(productionPlanParam);
                        break;
                }
                break;
            case WORK_ORDER:
                switch (microServiceEntity.getOperationType()) {
                    case ADD:

                        productionWorkOrderService.microServiceAdd(microServiceEntity.getObject());
                        productionCardService.addBatchCardByProductionPlan(microServiceEntity.getObject());

                }
                break;
            default:
        }
    }
}
