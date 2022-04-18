package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.entity.InstockOrder;
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


    public Boolean checkTask(Long orderId, RuleType ruleType){
        InstockOrder instockOrder = instockOrderService.getById(orderId);
        switch (ruleType) {

            case purchase_complete:
                if (instockOrder.getState().equals(98)) {
                    return true;
                } else {
                    return false;
                }

            default:
                return false;
        }
    }
}
