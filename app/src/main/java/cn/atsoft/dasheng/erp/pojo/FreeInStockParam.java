package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FreeInStockParam {
    @NotNull
    private Long storeHouseId;
    @NotNull
    private List<Long> inkindIds;
    @NotNull
    private Long positionsId;

    private String type;
}