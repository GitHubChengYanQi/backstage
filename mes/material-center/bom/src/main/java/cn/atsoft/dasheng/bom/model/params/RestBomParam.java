package cn.atsoft.dasheng.bom.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class RestBomParam {
    /**
     * 物料
     */
    @ApiModelProperty("skuId")
    private Long skuId;

    /**
     * 数量
     */
    @ApiModelProperty("number")
    private Integer number;
    /**
     * bomId
     */
    @ApiModelProperty("bomId")
    private Long bomId;

    /**
     * 部门Id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    /**
     * 版本
     */
    @ApiModelProperty("version")
    private String version;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keywords;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    private List<RestBomDetailParam> bomDetailParam;

}
