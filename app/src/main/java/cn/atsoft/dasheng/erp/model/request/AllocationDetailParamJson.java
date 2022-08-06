package cn.atsoft.dasheng.erp.model.request;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

import java.util.List;

@Data
public class AllocationDetailParamJson {
    private List<AllocationDetailParam> skuAndNumbers;
    private List<AllocationCartParam> storehouseAndPositions;
}
