package cn.atsoft.dasheng.db.model.result;

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
 * @since 2020-12-11
 */
@Data
@ApiModel
public class FieldConfigResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;

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

}
