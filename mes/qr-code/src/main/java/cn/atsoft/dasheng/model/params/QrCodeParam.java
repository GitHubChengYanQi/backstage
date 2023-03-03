package cn.atsoft.dasheng.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 二维码
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Data
@ApiModel
public class QrCodeParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String codeType;

    private String url;

    private List<Long> ids;

    private Integer addSize;
    /**
     * 绑定状态
     */
    private Integer state;
    /**
     * 码id
     */
    @ApiModelProperty("码id")
    private String orCodeId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

//    private String codeType;

    /**
     * 信息
     */
    @ApiModelProperty("信息")
    private String data;

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
