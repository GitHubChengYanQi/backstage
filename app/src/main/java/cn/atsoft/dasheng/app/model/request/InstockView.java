package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import lombok.Data;

import java.util.List;

@Data
public class InstockView {
    private Long customerId;
    private CustomerResult customerResult;
    private Integer logSkuCount;
    private Integer logNumberCount;
    private Integer detailSkuCount;
    private Integer detailNumberCount;
    private Integer errorSkuCount;
    private Integer errorNumberCount;
    private List<InstockLogDetail> instockLogDetails;
    private List<InstockList> instockLists;
}
