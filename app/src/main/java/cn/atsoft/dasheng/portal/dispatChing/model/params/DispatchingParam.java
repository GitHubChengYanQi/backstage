package cn.atsoft.dasheng.portal.dispatChing.model.params;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 派工表
 * </p>
 *
 * @author
 * @since 2021-08-23
 */
@Data
@ApiModel
public class DispatchingParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long type;

    private String page;

    /**
     * 派工id
     */
    @ApiModelProperty("派工id")
    private Long dispatchingId;

    /**
     * 姓名
     */
    @NotNull(message = "请选择工程师")
    @ApiModelProperty("姓名")
    private Long name;

    /**
     * 手机号
     */

//    @ApiParam(required = true)
//    @Pattern(regexp = "^1\\d{10}", message = "手机号码格式错误")
//    @NotBlank(message = "请填写手机号")
//    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 预计到达时间
     */
    @ApiModelProperty("预计到达时间")
    private Date time;

    /**
     * 负责区域
     */
    @ApiModelProperty("负责区域")
    private String address;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer state;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 完成照片
     */
    @ApiModelProperty("完成照片")
    private String imgUrl;
    private String evaluations;

    /**
     * 评价
     */
    @ApiModelProperty("评价")
    private String evaluation;

    /**
     * 报修id
     */
    @NotNull(message = "请确认报修信息")
    @ApiModelProperty("报修id")
    private Long repairId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
