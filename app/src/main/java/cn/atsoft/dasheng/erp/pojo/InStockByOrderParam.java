package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;
import org.omg.CORBA.LongHolder;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class InStockByOrderParam {
    @NotNull
    public List<SkuParam> skuParams;

    private String remark;

    private Date instockTime;

    private Long instockOrderId;

    private Long actionId;

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
