package cn.atsoft.dasheng.form.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工序步骤设置表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiSetpSetParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<ActivitiSetpSetDetailParam> setpSetDetailParam;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long setId;

    private String productionType;

    /**
     * 工位
     */
    @ApiModelProperty("工位Id")
    private Long productionStationId;
    /**
     * 步骤Id
     */
    @ApiModelProperty("步骤Id")
    private Long setpsId;

    /**
     * 工序Id
     */
    @ApiModelProperty("工序Id")
    private Long shipSetpId;
    //TODO 工序

    /**
     * 标准工作时长，按小时计算
     */
    @ApiModelProperty("标准工作时长，按小时计算")
    private Integer length;

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

    @Override
    public String checkParam() {
        return null;
    }

}
