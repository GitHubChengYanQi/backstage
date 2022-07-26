package cn.atsoft.dasheng.erp.model.request;

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
        private Integer number;
        private Long storehouseId;
        private Long storehousePositionsId;
    }

//    @Data
//    public static class StorehouseAndPosition{
//        private Long storehouseId;
//        private Long storehousePositionsId;
//    }

}
