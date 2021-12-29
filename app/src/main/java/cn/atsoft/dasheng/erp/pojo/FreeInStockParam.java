package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FreeInStockParam {
    private Long storeHouseId;
    @NotNull
    private List<Long> codeIds;
    private Long positionsId;
}
