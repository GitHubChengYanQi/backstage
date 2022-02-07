package cn.atsoft.dasheng.purchase.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProcurementAOG {
    @NotNull
    private Long orderId;  //采购单id

    @NotNull
    private List<AOGDetails> aogDetails;

    @Data
    public class AOGDetails {
        @NotNull
        private Long detailsId;   //采购单详情id
        @NotNull
        private Long number;     //到货数量
    }
}
