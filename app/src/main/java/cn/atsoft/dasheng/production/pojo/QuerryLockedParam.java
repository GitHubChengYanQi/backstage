package cn.atsoft.dasheng.production.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuerryLockedParam {
    @NotNull
    private Long positionId;
    @NotNull
    private Long brandId;
    @NotNull
    private Long skuId;
}
