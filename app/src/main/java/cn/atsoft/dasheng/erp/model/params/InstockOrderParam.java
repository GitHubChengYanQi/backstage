package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
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
 * 入库单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Data
@ApiModel
public class InstockOrderParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    //跳转路径
    private String url;

    private String customerName;

    private String type;
    /**
     * 库位id
     */
    private Long storehousePositionsId;

    private List<InstockRequest> instockRequest;

    private List<InstockListParam> listParams;

    /**
     * 来源
     */
    private String source;

    /**
     * 来源id
     */
    private Long sourceId;


    private Date time;
    private String coding;

    @ApiModelProperty("仓库id")
    private Long storeHouseId;
    /**
     * 入库单
     */
    @ApiModelProperty("入库单")
    private Long instockOrderId;

    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")
    private Date instockTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 库管人员负责人
     */
    @ApiModelProperty("库管人员负责人")
    private Long stockUserId;

    private Date registerTime;
    /**
     * 是否加急
     */
    @ApiModelProperty("是否加急")
    private Integer urgent;


    /**
     * 附件（最多5个，逗号分隔）
     */
    @ApiModelProperty("附件")
    private String enclosure;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String origin;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 入库状态
     */
    @ApiModelProperty("入库状态")
    private Integer state;

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
