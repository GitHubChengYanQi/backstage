package cn.atsoft.dasheng.view.model.params;

import cn.atsoft.dasheng.view.model.pojo.MobelViewJson;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 移动端菜单
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-09
 */
@Data
@ApiModel
public class MobelTableViewParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<MobelViewJson> details;

    @ApiModelProperty("")
    private Long mobelTableViewId;
    @ApiModelProperty("")
    private Integer type;

    @ApiModelProperty("")
    private Long userId;


    @ApiModelProperty("")
    private String field;

    @ApiModelProperty("")
    private String tableKey;

    @ApiModelProperty("")
    private String name;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
