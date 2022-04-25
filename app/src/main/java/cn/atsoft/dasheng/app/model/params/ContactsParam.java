package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 联系人表
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Data
@ApiModel
public class ContactsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private List<PhoneParam> phoneParams;


    private String deptName;

    private String positionName;

    private Long positionId;
    /**
     * 联系人id
     */
    @ApiModelProperty("联系人id")
    private Long contactsId;
    /**
     * 公司角色
     */
    private Long companyRole;

    /**
     * 联系人姓名
     */
    @ApiModelProperty("联系人姓名")
    private String contactsName;

    /**
     * 联系人备注
     */
    @ApiModelProperty("联系人备注")
    private String contactsRemark;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    private String job;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private Long phone;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    @NotNull
    private Long customerId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
