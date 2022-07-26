package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

@Data
public class PositionNum {
    private Long positionId;
    private Long storehouseId;
    private Long toPositionId;
    private Long toStorehouseId;
    private Long num;


    private StorehousePositionsResult positionsResult;
    private StorehousePositionsResult toPositionsResult;

    public PositionNum(Long positionId, Long storehouseId, Long toPositionId, Long toStorehouseId, Long num) {
        this.positionId = positionId;
        this.storehouseId = storehouseId;
        this.toPositionId = toPositionId;
        this.toStorehouseId = toStorehouseId;
        this.num = num;
    }

    public PositionNum(Long positionId, Long num) {
        this.positionId = positionId;
        this.num = num;
    }
}
