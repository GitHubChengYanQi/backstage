package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

import java.util.List;

@Data
public class MaintenanceAndInventorySelectParam {
    private List<Long> materialIds;

    private List<Long> brandIds;

    private List<Long> storehousePositionsIds;

    private List<Long> partsIds;

    private List<Long> spuClassificationIds;


    private List<MaterialResult>  materialResults;

    private List<BrandResult> brandResults;

    private List<StorehousePositionsResult> storehousePositionsResults;

    private List<PartsResult> partsResults;

    private List<SpuClassificationResult> spuClassificationResults;
}
