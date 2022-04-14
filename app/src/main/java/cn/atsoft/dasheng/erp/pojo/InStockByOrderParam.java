package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class InStockByOrderParam {
    @NotNull
    public List<SkuParam> skuParams;

    private String remark;

    private Date instockTime;

    @Data
    public class SkuParam {
        @NotNull
        private Long inStockListId;
        @NotNull
        private Long skuId;
        @NotNull
        private List<Long> inkindIds;
        @NotNull
        private Long positionId;
        private Long stockNumber;
    }
}
