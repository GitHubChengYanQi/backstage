package cn.atsoft.dasheng.view.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class MobelTableViewResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long mobelTableViewId;

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
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门编号")
    private Long deptId;
    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
