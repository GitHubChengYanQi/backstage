package cn.atsoft.dasheng.query.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 搜索查询记录
 * </p>
 *
 * @author song
 * @since 2022-05-19
 */
@Data
@ApiModel
public class QueryLogResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 查询记录id
     */
    @ApiModelProperty("查询记录id")
    private Long queryLogId;

    /**
     * 表单类型
     */
    @ApiModelProperty("表单类型")
    private String formType;

    /**
     * 记录
     */
    @ApiModelProperty("记录")
    private String record;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
