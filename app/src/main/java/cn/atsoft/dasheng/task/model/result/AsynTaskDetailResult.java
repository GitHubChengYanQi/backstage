package cn.atsoft.dasheng.task.model.result;

import cn.atsoft.dasheng.Excel.pojo.PositionBind;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.Excel.pojo.SpuExcel;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 任务详情表
 * </p>
 *
 * @author song
 * @since 2022-04-26
 */
@Data
@ApiModel
public class AsynTaskDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SkuExcelItem skuExcelItem;

    private SpuExcel spuExcel;

    private PositionBind positionBind;

    private String type;
    /**
     * 详情id
     */
    @ApiModelProperty("详情id")
    private Long detailId;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long taskId;

    /**
     * json数据
     */
    @ApiModelProperty("json数据")
    private String contentJson;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
