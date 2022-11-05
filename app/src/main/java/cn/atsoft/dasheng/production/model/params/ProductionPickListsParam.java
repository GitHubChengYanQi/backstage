package cn.atsoft.dasheng.production.model.params;

import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
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
 * 领料单
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Data
@ApiModel
public class ProductionPickListsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private Long taskId;
    private List<ProductionPickListsDetailParam> pickListsDetailParams;
    private List<ProductionPickListsCartParam> cartsParams;
    private List<Long> pickListsIds;
    private String pickListsName;
    private String pickCode;
    private String remark;
    private String theme;
    private List<Long> remarkUserIds;
    @ApiModelProperty("redisCode")
    private String code;
    @ApiModelProperty("主任务id")
    private Long mainTaskId;

    private LoginUser loginUser;



    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;
    /**
     * 附件
     */
    @ApiModelProperty("关联人（多）")
    private String userIds;
    /**
     * remarks
     */
    @ApiModelProperty("注意事项")
    private String remarks;

    /**
     * note
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 领料单
     */
    @ApiModelProperty("领料单")
    private Long pickListsId;

    @ApiModelProperty("")
    private Long userId;

    /**
     * 领取物料码
     */
    @ApiModelProperty("领取物料码")
    private String coding;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long sourceId;

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

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
