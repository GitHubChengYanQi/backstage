package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 工艺路线列表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
@Data
@ApiModel
public class ProcessRouteResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuResult skuResult;
    private ActivitiStepsResult stepsResult;
    private List<ActivitiSetpSetResult> setpSetResults;
    /**
     * 工艺路线id
     */
    @ApiModelProperty("工艺路线id")
    private Long processRouteId;

    private Long skuId;

    private String type;

    private Long pid;

    /**
     * 工艺路线编号
     */
    @ApiModelProperty("工艺路线编号")
    private String processRouteCoding;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty("工艺路线名称")
    private String processRoteName;

    /**
     * 关联工艺物料清单
     */
    @ApiModelProperty("关联工艺物料清单")
    private Long partsId;

    /**
     * 版本号
     */
    @ApiModelProperty(hidden = true)
    private Long version;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
