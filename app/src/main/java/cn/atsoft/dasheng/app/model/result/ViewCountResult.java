package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

@Data
public class ViewCountResult {
    private Integer skuCount;
    private Integer customerCount;
    private Integer brandCount;
    private Integer storeHouseCount;
    private Integer positionCount;
    private Integer classCount;

}
