package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.List;

@Data

public class SkuBindParam {

    private List<Long> spuIds;

    private List<Long> classIds;

    private List<Long> brandIds;

    private List<Long> positionIds;

    private List<Long> bomIds;

    private List<Long> skuIds;

}
