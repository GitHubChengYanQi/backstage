package cn.atsoft.dasheng.erp.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 异常详情
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
@Data
@ApiModel
public class AnomalyDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long planNumber;

    private Long realNumber;


    /**
     * 实物id
     */
    private Long inkindId;


    private Long number;

    private Long instockListId;
    /**
     * 详情id
     */
    @ApiModelProperty("详情id")
    private Long detailId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long anomalyId;

    @NotNull
    @ApiModelProperty("")
    private Long skuId;

    /**
     * 异常类型
     */
    @ApiModelProperty("异常类型")
    private String type;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

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
