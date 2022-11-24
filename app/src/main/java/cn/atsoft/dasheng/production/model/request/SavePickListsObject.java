package cn.atsoft.dasheng.production.model.request;

import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import lombok.Data;

import java.util.List;

@Data
public class SavePickListsObject {
    private List<ProductionTaskDetail> details;
    private ProductionTask productionTask;
    private LoginUser loginUser;

}
