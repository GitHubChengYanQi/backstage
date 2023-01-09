package cn.atsoft.dasheng.general.model.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class BomListResult implements Serializable {
    private Long skuId;
    private String version;
    private String skuName;
    private String standard;
    private String model;
    private Long partsId;
    private String partsName;
    private String spuName;
}
