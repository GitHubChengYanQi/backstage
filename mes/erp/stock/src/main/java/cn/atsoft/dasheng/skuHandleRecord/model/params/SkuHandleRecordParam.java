package cn.atsoft.dasheng.skuHandleRecord.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * sku 任务操作记录
 * </p>
 *
 * @author
 * @since 2022-10-25
 */
@Data
@ApiModel
public class SkuHandleRecordParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<String> types;

    private List<Long> userIds;

    private List<Long> brandIds;

    private List<Long> positionIds;


    private String startTime;

    private String endTime;


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long recordId;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源的任务单据
     */
    @ApiModelProperty("来源的任务单据")
    private Long sourceId;

    /**
     * 当时库存数
     */
    @ApiModelProperty("当时库存数")
    private Long nowStockNumber;

    /**
     * 操作数量
     */
    @ApiModelProperty("操作数量")
    private Long operationNumber;

    /**
     * 结余数量
     */
    @ApiModelProperty("结余数量")
    private Long balanceNumber;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private Long brandId;

    /**
     * 库位id
     */
    @ApiModelProperty("库位id")
    private Long positionId;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 处理时间
     */
    @ApiModelProperty("处理时间")
    private Date operationTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private Long operationUserId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
