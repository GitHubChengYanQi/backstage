package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.service.AnomalyOrderService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CheckInstock {

    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;


    public Boolean checkTask(Long orderId, RuleType ruleType) {
        AnomalyOrder anomalyOrder = anomalyOrderService.getById(orderId);
        switch (ruleType) {

            case status:
                return true;

            default:
                return false;
        }
    }
}
