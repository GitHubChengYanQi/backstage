package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.List;

@Data
public class SkuListParam {

    private String keyWord;

    private Long partsId;

    private Long categoryId;

    private Long classId;

    private Integer minimumInventory;
    
    private Integer maximumInventory;

    //是否有bom
    private Boolean bomNum;

    //是否有工艺
    private Boolean shipNum;

    //品牌id
    private List<Long> brandIds;

    //供应商id
    private List<Long> customerIds;

    //材质id
    private List<Long> materialIds;

    //库位id
    private List<Long> positionIds;

    //仓库id
    private List<Long> storeIds;

}
