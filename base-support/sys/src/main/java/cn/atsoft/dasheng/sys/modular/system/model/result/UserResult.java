package cn.atsoft.dasheng.sys.modular.system.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 
 * @since 2021-08-05
 */
@Data
@ApiModel
public class UserResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long userId;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * md5密码盐
     */
    @ApiModelProperty("md5密码盐")
    private String salt;

    /**
     * 名字
     */
    @ApiModelProperty("名字")
    private String name;

    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private Date birthday;

    /**
     * 性别(字典)
     */
    @ApiModelProperty("性别(字典)")
    private String sex;

    /**
     * 电子邮件
     */
    @ApiModelProperty("电子邮件")
    private String email;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 角色id(多个逗号隔开)
     */
    @ApiModelProperty("角色id(多个逗号隔开)")
    private String roleId;

    /**
     * 部门id(多个逗号隔开)
     */
    @ApiModelProperty("部门id(多个逗号隔开)")
    private Long deptId;

    /**
     * 状态(字典)
     */
    @ApiModelProperty("状态(字典)")
    private String status;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 乐观锁
     */
    @ApiModelProperty(hidden = true)
    private Integer version;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
