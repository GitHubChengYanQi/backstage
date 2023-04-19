package cn.atsoft.dasheng.production.model;

import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import lombok.Data;

import java.util.List;
@Data
public class CreateProductionTask {
    private ProductionTask productionTask;
    private List<ProductionTaskDetail> details;
}
