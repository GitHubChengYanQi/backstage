package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.List;

@Data
public class InStockByOrderParam {

    public List<SkuParam> skuParams;

    @Data
    public class SkuParam {
        private Long inStockListId;
        private Long skuId;
        private List<Long> inkindIds;
        private Long positionId;
        private Long stockNumber;
    }
}
