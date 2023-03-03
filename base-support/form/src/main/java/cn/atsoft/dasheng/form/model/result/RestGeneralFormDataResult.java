package cn.atsoft.dasheng.form.model.result;

import com.alibaba.fastjson.annotation.JSONField;
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
public class RestGeneralFormDataResult implements Serializable {

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
    @JSONField(serialize = false)
    private Integer sourceId;

    @ApiModelProperty("")
    @JSONField(serialize = false)
    private String source;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    @JSONField(serialize = false)
    private Integer display;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date createTime;


    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    @ApiModelProperty("父ID顺序数组")
    @JSONField(serialize = false)
    private List<String> pidValue;
}
