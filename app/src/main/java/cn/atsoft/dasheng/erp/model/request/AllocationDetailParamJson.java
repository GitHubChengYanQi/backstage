package cn.atsoft.dasheng.erp.model.request;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

import java.util.List;

@Data
public class AllocationDetailParamJson {
    private List<SkuAndNumber> skuAndNumbers;
    private List<SkuAndNumber> storehouseAndPositions;


    @Data
    public static class SkuAndNumber{
        private Long skuId;
        private Long brandId;
        private Long storehouseId;
        private Long storehousePositionsId;

        private Integer number;

        private SkuSimpleResult skuResult;
        private BrandResult brandResult;
        private StorehousePositionsResult storehousePositionsResult;
        private StorehouseResult storehouseResult;
    }

//    @Data
//    public static class StorehouseAndPosition{
//        private Long storehouseId;
//        private Long storehousePositionsId;
//    }

}
