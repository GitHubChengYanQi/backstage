package cn.atsoft.dasheng.form.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 自定义表单各个字段数据	
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
@Data
@ApiModel
public class DataValueResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
    private Long valueId;

    /**
     * 数据Id
     */
    @ApiModelProperty("数据Id")
    private Long dataId;

    /**
     * 字段
     */
    @ApiModelProperty("字段")
    private Long field;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

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
