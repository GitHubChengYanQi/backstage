package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 异常单据
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
@Data
@ApiModel
public class AnomalyResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<AnomalyDetailResult> results;

    private InstockOrderResult orderResult;

    private List<String> filedUrls;

    private String remark;

    private String enclosure;

    /**
     * 异常id
     */
    @ApiModelProperty("异常id")
    private Long anomalyId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 单据id
     */
    @ApiModelProperty("单据id")
    private Long formId;

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
}
