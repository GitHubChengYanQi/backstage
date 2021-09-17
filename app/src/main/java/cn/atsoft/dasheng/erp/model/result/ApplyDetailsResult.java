package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 
 * </p>
 *
 * @author song
 * @since 2021-09-15
 */
@Data
@ApiModel
public class ApplyDetailsResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private BrandResult brandResult;
    private ItemsResult itemsResult;

    /**
     * 发货申请详情id
     */
    @ApiModelProperty("发货申请详情id")
    private Long outstockApplyDetailsId;

    @ApiModelProperty("收货地址id")
    private Long adressId;

    @ApiModelProperty("联系人id")
    private Long contactsId;

    @ApiModelProperty("电话id")
    private Long phoneId;

    @ApiModelProperty("预计到达时间")
    private Date time;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long itemId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 发货申请id
     */
    @ApiModelProperty("发货申请id")
    private Long outstockApplyId;

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
    @ApiModelProperty("部门Id")
    private Long deptId;
    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
