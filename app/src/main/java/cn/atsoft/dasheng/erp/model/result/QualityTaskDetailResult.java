package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class QualityTaskDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuResult skuResult;
    private BrandResult brand;
    private QualityPlanResult qualityPlanResult;

    private List<BackSku> backSkus;

    private List<Long> userIdList;

    private List<String> users;
    @ApiModelProperty("")
    private Long qualityTaskDetailId;

    private String status;

    private String inkindId;

    private List<User> userList;

    private QualityTaskResult qualityTaskResult;
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

    private Integer batch;

    private Long instockNumber;


    /**
     * 百分比
     */
    private Double percentum;

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

    /**
     * 负责人s
     */
    private String userIds;

    /**
     * 时间
     */
    private Date time;

    private Long parentId;

    /**
     * 地点
     */
    private String address;

    /**
     * 对接人
     */
    private String person;

    private String phone;

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
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
