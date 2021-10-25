package cn.atsoft.dasheng.erp.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工具表
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
@Data
@ApiModel
public class ToolParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private String coding;
    /**
     * 操作规范
     */

    private String norm;
    /**
     * 工具id
     */
    @ApiModelProperty("工具id")
    private Long toolId;

    /**
     * 工具名称
     */
    @ApiModelProperty("工具名称")
    private String name;

    /**
     * 工具状态
     */
    @ApiModelProperty("工具状态")
    private Integer state;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String attachment;

    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private Long unitId;

    /**
     * 工具分类id
     */
    @ApiModelProperty("工具分类id")
    private Long toolClassificationId;

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
