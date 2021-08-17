package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.portal.model.validator.BaseValidatingParam;
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
 * @author Sing
 * @since 2021-03-17
 */
@Data
@ApiModel
public class UcOpenUserInfoParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private String primaryKey;

    /**
     * 会员编号
     */
    @ApiModelProperty("会员编号")
    private Long memberId;

    /**
     * 用户第三方系统的唯一id
     */
    @ApiModelProperty("用户第三方系统的唯一id")
    private String uuid;

    /**
     * 用户来源
     */
    @ApiModelProperty("用户来源")
    private String source;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户网址
     */
    @ApiModelProperty("用户网址")
    private String blog;

    /**
     * 所在公司
     */
    @ApiModelProperty("所在公司")
    private String company;

    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String location;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    @ApiModelProperty("用户备注（各平台中的用户个人介绍）")
    private String remark;

    /**
     * 性别|1男|0女|-1未知
     */
    @ApiModelProperty("性别|1男|0女|-1未知")
    private Integer gender;

    /**
     * 第三方平台返回的原始用户信息
     */
    @ApiModelProperty("第三方平台返回的原始用户信息")
    private String rawUserInfo;

    /**
     * 手机号码，关联用户
     */
    @ApiModelProperty("手机号码，关联用户")
    private String mobile;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
