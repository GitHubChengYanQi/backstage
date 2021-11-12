package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
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
    /**
     * 库位id
     */
    private Long storehousePositionsId;

    private List<InstockRequest> instockRequest;

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
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

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
    private Boolean state;

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
