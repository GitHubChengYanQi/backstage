package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 质检任务
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@Data
@ApiModel
public class QualityTaskParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private Long microUserId;
    private static final long serialVersionUID = 1L;
    private List<QualityTaskDetailParam> details;

    private String url;

    /**
     * 多个人负责人
     */
    private String userIds;

    /**
     * 地点
     */
    private String address;

    /**
     * 接头人
     */
    private String person;
    /**
     * 电话
     */
    private String phone;
    /**
     * 备注
     */
    private String note;
    /**
     * 父级id
     */
    private Long parentId;


    @ApiModelProperty("")
    private Long qualityTaskId;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    private Date time;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;


    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private Long source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private String sourceId;
    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;
    /**
     * 来源Json
     */
    @ApiModelProperty("来源Json")
    private String origin;

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

    private Integer state;

    private Long status;

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

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
