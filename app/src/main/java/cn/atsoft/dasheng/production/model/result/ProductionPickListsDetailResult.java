package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 领料单详情表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Data
@ApiModel
public class ProductionPickListsDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuResult skuResult;
    private Integer status;

    private Long storehousePositionsId;
    private Long storehouseId;
    private UserResult userResult;
    private String pickListsCoding;
    /**
     * 子表id
     */
    @ApiModelProperty("子表id")
    private Long pickListsDetailId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long pickListsId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;

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
