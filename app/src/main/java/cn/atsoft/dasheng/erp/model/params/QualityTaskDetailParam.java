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
 * 质检任务详情
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@Data
@ApiModel
public class QualityTaskDetailParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private List<Long> detailsIds;


    @ApiModelProperty("")
    private Long qualityTaskDetailId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long qualityTaskId;

    /**
     * 质检项
     */
    @ApiModelProperty("质检项")
    private Long qualityPlanId;
    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private Long brandId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;

    private Integer remaining;

    private Integer batch;

    /**
     * 负责人s
     */
    private String userIds;

    /**
     * 时间
     */
    private Date time;

    /**
     * 地点
     */
    private String address;

    private String phone;

    private String note;

    /**
     * 对接人
     */
    private Long person;

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

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
