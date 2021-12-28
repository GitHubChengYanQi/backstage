package cn.atsoft.dasheng.printTemplate.model.params;

import cn.atsoft.dasheng.form.pojo.printTemplateEnum;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 编辑模板
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-28
 */
@Data
@ApiModel
public class PrintTemplateParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 编辑模板id
     */
    @ApiModelProperty("编辑模板id")
    private Long printTemplateId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private printTemplateEnum type;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 模板
     */
    @ApiModelProperty("模板")
    private String templete;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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

    @Override
    public String checkParam() {
        return null;
    }

}
