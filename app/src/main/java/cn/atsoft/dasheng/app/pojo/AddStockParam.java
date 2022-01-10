package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

@Data
public class AddStockParam {
    private Long skuId;
    private Long brandId;
    private Long storeHouseId;
    private Long positionsId;
    private Long number;
    private Long codeId;
    private Long inkindId;

    public AddStockParam(Long skuId, Long brandId, Long storeHouseId, Long positionsId, Long number, Long codeId, Long inkindId) {
        this.skuId = skuId;
        this.brandId = brandId;
        this.storeHouseId = storeHouseId;
        this.positionsId = positionsId;
        this.number = number;
        this.codeId = codeId;
        this.inkindId = inkindId;
    }
}
