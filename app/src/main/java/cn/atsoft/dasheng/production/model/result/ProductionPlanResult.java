package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 生产计划主表
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
@Data
@ApiModel
public class ProductionPlanResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ProductionPlanDetailResult> planDetailResults;
    private Integer skuCount;
    private UserResult userResult;
    private List<UserResult> userList;

    private Integer bomCount;
    private Integer receivedCount;

    private Integer numberCount;

    private Integer cartCount;
    private Integer doneCartCount;
    private Integer doneBomCount;

    private UserResult createUserResult;


    private List<ProductionWorkOrderResult> workOrderResults;


    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

    /**
     * 生产计划id
     */
    @ApiModelProperty("编码")
    private String coding;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    private String origin;

    /**
     * 执行时间
     */
    @ApiModelProperty("执行时间")
    private Date executionTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String userId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 卡片编码
     */
    @ApiModelProperty("卡片编码")
    private String cardCoding;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 文件
     */
    @ApiModelProperty("文件")
    private String files;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
