package cn.atsoft.dasheng.purchase.service.impl;

import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckPurchaseAsk {

    @Autowired
    private PurchaseAskService purchaseAskService;
    public Boolean checkTask(Long purchaseAskId, RuleType ruleType){
        PurchaseAsk purchaseAsk = purchaseAskService.getById(purchaseAskId);
        switch (ruleType) {

            case purchase_complete:
                if (purchaseAsk.getStatus().equals(2)) {
                    return true;
                } else {
                    return false;
                }

            default:
                return false;
        }
    }
}
