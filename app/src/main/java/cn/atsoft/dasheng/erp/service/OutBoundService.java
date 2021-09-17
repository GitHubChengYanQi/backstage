package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;

public interface OutBoundService {
    /**
     * 出库
     *
     * @param
     * @param stockHouseId
     * @return
     */
    String judgeOutBound(Long outstockOrderId, Long stockHouseId);

    String aKeyDelivery(OutstockApplyParam outstockApplyParam);
}
