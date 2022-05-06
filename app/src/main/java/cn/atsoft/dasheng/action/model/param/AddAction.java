package cn.atsoft.dasheng.action.model.param;

import cn.atsoft.dasheng.action.Enum.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddAction {


    public List<Action> actions;

    @NotNull
    public ReceiptsEnum receiptsEnum;

    @Data
    public class Action {
        @NotNull
        public Long statusId;

        public List<PurchaseActionEnum> purchaseActionEnums;

        public List<InStockActionEnum> inStockActionEnums;

        public List<PoOrderActionEnum> poOrderActionEnums;

        public List<OutStockActionEnum> outStockActionEnums;

        public List<InstockErrorActionEnum> instockErrorActionEnums;

        public List<QualityActionEnum> qualityActionEnums;

    }
}
