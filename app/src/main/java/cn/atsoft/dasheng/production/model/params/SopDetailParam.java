package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * sop 详情
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class SopDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 详情id
     */
    @ApiModelProperty("详情id")
    private Long sopDetailId;

    /**
     * sop
     */
    @ApiModelProperty("sop")
    private Long sopId;

    /**
     * 步骤名
     */
    @ApiModelProperty("步骤名")
    private String stepName;

    /**
     * 示例图
     */
    @ApiModelProperty("示例图")
    private String img;

    /**
     * 操作说明
     */
    @ApiModelProperty("操作说明")
    private String instructions;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;



    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
