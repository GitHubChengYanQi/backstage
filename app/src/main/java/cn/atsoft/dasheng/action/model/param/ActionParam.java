package cn.atsoft.dasheng.action.model.param;

import cn.atsoft.dasheng.action.Enum.PurchaseActionEnum;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActionParam {

    private PurchaseActionEnum purchaseActionEnum;

}
