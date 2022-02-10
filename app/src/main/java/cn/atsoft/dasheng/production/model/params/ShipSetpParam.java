package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工序表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class ShipSetpParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private Long shipSetpId;

    /**
     * 工序名称	
     */
    @ApiModelProperty("工序名称	")
    private String shipSetpName;

    /**
     * 工序分类
     */
    @ApiModelProperty("工序分类")
    private Long shipSetpClass;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 人员id
     */
    @ApiModelProperty("人员id")
    private Long userId;

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long productionStationId;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String accessories;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
