package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * sop 主表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class SopResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SopDetailResult> sopDetails;
    private Long pid;
    private List<SopResult> oldSop;
    private User user;
    private List<String> mediaUrls;
    private ShipSetpResult shipSetpResult;
    /**
     * 修改原因
     */
    private String alterWhy;
    /**
     * sopId
     */
    @ApiModelProperty("sopId")
    private Long sopId;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String coding;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private Long shipSetpId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String versionNumber;

    /**
     * 成品图
     */
    @ApiModelProperty("成品图")
    private String finishedPicture;

    /**
     * 注意事项
     */
    @ApiModelProperty("注意事项")
    private String note;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
