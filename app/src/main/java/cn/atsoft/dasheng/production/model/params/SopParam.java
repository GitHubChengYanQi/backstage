package cn.atsoft.dasheng.production.model.params;

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
 * sop 主表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class SopParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<SopDetailParam> sopDetails;

    private List<Long> sopIds;

    /**
     * sopId
     */
    @ApiModelProperty("sopId")
    private Long sopId;


    private Long pid;

    /**
     * 修改原因
     */
    private String alterWhy;
    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String coding;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private Long shipSetpId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String versionNumber;

    /**
     * 成品图
     */
    @ApiModelProperty("成品图")
    private String finishedPicture;

    /**
     * 注意事项
     */
    @ApiModelProperty("注意事项")
    private String note;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
