package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

@Data
public class PositionNum {
    private Long positionId;
    private Long num;

    public PositionNum(Long positionId, Long num) {
        this.positionId = positionId;
        this.num = num;
    }
}
