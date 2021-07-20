package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 客户管理表
 * </p>
 *
 * @author ta
 * @since 2021-07-19
 */
@Data
@ApiModel
public class ClientParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long clientId;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String name;

    /**
     * 客户地址id
     */
    @ApiModelProperty("客户地址id")
    private String adressId;

    /**
     * 联系人id
     */
    @ApiModelProperty("联系人id")
    private Long contactsId;

    /**
     * 固定电话
     */
    @ApiModelProperty("固定电话")
    private Long tel;

    /**
     * 成立时间
     */
    @ApiModelProperty("成立时间")
    private Date setup;

    /**
     * 法定代表人
     */
    @ApiModelProperty("法定代表人")
    private String legal;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String utscc;

    /**
     * 公司类型
     */
    @ApiModelProperty("公司类型")
    private String companyType;

    /**
     * 营业期限
     */
    @ApiModelProperty("营业期限")
    private String businessTerm;

    /**
     * 注册地址
     */
    @ApiModelProperty("注册地址")
    private String signIn;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String introduction;

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

    @Override
    public String checkParam() {
        return null;
    }

}