package cn.atsoft.dasheng.db.model.result;

import cn.atsoft.dasheng.base.db.model.TableFieldInfo;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 字段配置
 * </p>
 *
 * @author Sing
 * @since 2020-12-12
 */
@Data
@ApiModel
public class FieldConfigResult extends TableFieldInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long fieldId;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String table;

    /**
     * 字段类型
     */
    @ApiModelProperty("字段类型")
    private String type;

    /**
     * 数据配置
     */
    @ApiModelProperty("数据配置")
    private String config;

    /**
     * 是否列表显示
     */
    @ApiModelProperty("是否列表显示")
    private Boolean showList;

    /**
     * 是否搜索
     */
    @ApiModelProperty("是否搜索")
    private Boolean isSearch;

}
