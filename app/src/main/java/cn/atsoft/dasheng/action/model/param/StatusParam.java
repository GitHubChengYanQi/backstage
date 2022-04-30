package cn.atsoft.dasheng.action.model.param;

import cn.atsoft.dasheng.action.Enum.OrderEnum;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import cn.atsoft.dasheng.form.model.params.DocumentsStatusParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StatusParam {

    private OrderEnum orderEnum;

    @NotNull
    private DocumentsStatusParam param;
}
