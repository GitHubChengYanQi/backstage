package cn.atsoft.dasheng.sys.modular.system.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.DeptDto;
import cn.atsoft.dasheng.sys.modular.system.model.RoleDto;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @since 2021-08-06
 */
@Data
@ApiModel
public class UserResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;
    /**
     * 部门返回
     */
    private DeptDto deptResult;
    /**
     * 微信openId
     */
    private String  openId;

    /**
     * miniApp头像
     */

    private String miniAppAvatar;

    /**
     * 角色返回
     */
    private List<RoleDto> roleResults;

    /**
     * 主键id
     */

    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long userId;

    /**
     * 头像
     */
    @ApiModelProperty(hidden = true)
    private String avatar;

    /**
     * 账号
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String account;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * md5密码盐
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
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
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String email;

    /**
     * 电话
     */
    @ApiModelProperty(hidden = true)
    private String phone;

    /**
     * 角色id(多个逗号隔开)
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String roleId;

    /**
     * 部门id(多个逗号隔开)
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long deptId;

    /**
     * 状态(字典)
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态(字典)")
    private String status;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建人
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 更新时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 更新人
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 乐观锁
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Integer version;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
