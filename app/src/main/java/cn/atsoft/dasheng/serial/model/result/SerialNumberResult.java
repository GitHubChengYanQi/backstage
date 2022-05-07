package cn.atsoft.dasheng.serial.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 流水号
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
@Data
@ApiModel
public class SerialNumberResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 流水号id
     */
    @ApiModelProperty("流水号id")
    private Long serialId;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    private Date date;

    /**
     * 样式
     */
    @ApiModelProperty("样式")
    private String model;

    /**
     * 流水号
     */
    @ApiModelProperty("流水号")
    private Long num;

    /**
     * 唯一字符串
     */
    @ApiModelProperty("唯一字符串")
    private String md5;

    /**
     * 长度
     */
    @ApiModelProperty("长度")
    private Long serialLength;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

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
