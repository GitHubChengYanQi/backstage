package cn.atsoft.dasheng.app.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 合同模板
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Data
@ApiModel
public class TemplateParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long fileId;
    /**
     * 合同分类id
     */
    private Long contractClassId;
    /**
     * 合同模板id
     */
    @ApiModelProperty("合同模板id")
    private Long templateId;

    /**
     * 合同姓名
     */
    @ApiModelProperty("合同姓名")
    private String name;

    /**
     * 合同内容
     */
    @ApiModelProperty("合同内容")
    private String content;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;
    @Override
    public String checkParam() {
        return null;
    }

}
