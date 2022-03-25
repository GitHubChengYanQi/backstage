package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author cheng
 * @since 2021-08-12
 */
@Data
@ApiModel
public class PhoneParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 电话id
     */

    @ApiModelProperty("电话id")
    private Long phoneId;

    private String telephone;

    /**
     * 联系人id
     */
    @ApiModelProperty("联系人id")
    private Long contactsId;

    /**
     * 电话号码
     */
//    @Pattern(regexp = "^1\\d{10}", message = "手机号码格式错误")
//    @NotNull(message = "电话不能为空")
//    @Length(min = 11, message = "手机最少位数是{min}")
//    @ApiModelProperty("电话号码")
    private Long phoneNumber;

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
    private Long deptId;

    @Override
    public String checkParam() {
        return null;
    }

}
