package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 调拨主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@Data
@ApiModel
public class AllocationParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<AllocationDetailParam> detailParams;

    /**
     * 调拨id
     */
    @ApiModelProperty("调拨id")
    private Long allocationId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    @ApiModelProperty("")
    private String allocationName;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long storehouseId;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    @ApiModelProperty("")
    private String reason;

    @ApiModelProperty("")
    private String remark;

    @ApiModelProperty("")
    private String note;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;

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

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    private String origin;

    /**
     * 库间调拨  仓库调拨
     */
    @ApiModelProperty("库间调拨  仓库调拨")
    private Integer type;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
