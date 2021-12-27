package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import lombok.Data;

import java.util.List;

@Data
public class BackObject {
    private StockDetails stockDetails;
    private StorehousePositions positions;
}
