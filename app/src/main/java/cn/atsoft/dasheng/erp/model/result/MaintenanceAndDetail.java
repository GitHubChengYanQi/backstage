package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import lombok.Data;

import java.util.List;

@Data
public class MaintenanceAndDetail {
    private List<Maintenance> maintenances;
    private List<MaintenanceDetail> maintenanceDetails;
}
