package cn.atsoft.dasheng.form.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-09-08
 */
@Data
@ApiModel
public class RestGeneralFormDataParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long  id;

    /**
     * 字段
     */
    @ApiModelProperty("字段")
    private String tableName;

    @ApiModelProperty("")
    private String fieldName;

    @ApiModelProperty("")
    private String value;

    @ApiModelProperty("")
    private Integer sourceId;

    @ApiModelProperty("")
    private String source;

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
