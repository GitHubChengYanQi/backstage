package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.request.InstockViewDetail;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.List;

@Data
public class InstockView {
    private Long customerId;
    private String customerName;
    private Integer logSkuCount;
    private Integer logNumberCount;
    private Integer detailSkuCount;
    private Integer detailNumberCount;
    private Integer errorSkuCount;
    private Integer errorNumberCount;
    private List<InstockLogDetailResult> instockLogDetails;
    private List<InstockListResult> instockLists;
    private List<AnomalyResult> errorList;
    private List<InstockOrder> instockOrders;
    private List<InstockViewDetail> instockViewDetails;
    private List<UserResult> userResults;
    private List<SkuSimpleResult> skuSimpleResults;
    private Integer orderCount;
}
