package cn.atsoft.dasheng.erp.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class BomParam {
    /**
     * 物料
     */
    @ApiModelProperty("skuId")
    private Long skuId;

    /**
     * 版本
     */
    @ApiModelProperty("version")
    private String version;

    private List<BomDetailParam> bomDetailParam;

}
