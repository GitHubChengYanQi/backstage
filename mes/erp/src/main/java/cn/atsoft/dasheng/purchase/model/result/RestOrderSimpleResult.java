package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class RestOrderSimpleResult implements Serializable {



    private static final long serialVersionUID = 1L;


    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;
    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

}
