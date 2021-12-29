package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FreeInStockParam {
    @NotNull
    private Long storeHouseId;
    @NotNull
    private List<Long> codeIds;
    @NotNull
    private Long positionsId;
}
