package cn.atsoft.dasheng.form.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单主表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class FormDataResult implements Serializable {

    private static final long serialVersionUID = 1L;
    List<Map<String ,Object>> valueResults;
    private Object inkind;
    private Object sku;
    private Object brand;


    /**
     * 数据主键
     */
    @ApiModelProperty("数据主键")
    private Long dataId;

    /**
     * 所属模块
     */
    @ApiModelProperty("所属模块")
    private String module;

    /**
     * 自定义表单Id
     */
    @ApiModelProperty("自定义表单Id")
    private Long formId;

    /**
     * 0为主表数据
     */
    @ApiModelProperty("0为主表数据")
    private Long mainId;

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
