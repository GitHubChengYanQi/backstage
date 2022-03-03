package cn.atsoft.dasheng.form.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 工序步骤详情表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiSetpSetDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object skuResult;

    private String equals;
    /**
     * 详情Id
     */
    @ApiModelProperty("详情Id")
    private Long detailId;

    private String productionType;

    /**
     * 步骤设置Id
     */
    @ApiModelProperty("步骤设置Id")
    private Long setpsId;

    /**
     * in（投入），out（产出）
     */
    @ApiModelProperty("in（投入），out（产出）")
    private String type;

    /**
     * 产品物料Id
     */
    @ApiModelProperty("产品物料Id")
    private Long spuId;

    /**
     * skuId
     */
    @ApiModelProperty("skuId")
    private Long skuId;

    /**
     * 需要数量
     */
    @ApiModelProperty("需要数量")
    private Integer num;

    /**
     * 0为主要材料，有值代表替换物，值对应的是detail_id
     */
    @ApiModelProperty("0为主要材料，有值代表替换物，值对应的是detail_id")
    private Long parentId;

    /**
     * 质检方案Id
     */
    @ApiModelProperty("质检方案Id")
    private Long qualityId;

    /**
     * 自检方案Id
     */
    @ApiModelProperty("质检方案Id")
    private Long myQualityId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
