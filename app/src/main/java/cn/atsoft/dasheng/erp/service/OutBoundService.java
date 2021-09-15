package cn.atsoft.dasheng.erp.service;

public interface OutBoundService {
    /**
     * 出库
     * @param applyId
     * @param stockHouseId
     * @return
     */
    String judgeOutBound(Long applyId,Long stockHouseId);
}
