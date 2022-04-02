package cn.atsoft.dasheng.erp.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataValueRequestParam {
    private FormValues formValues;
    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
    private Long valueId;

    /**
     * 数据Id
     */
    @ApiModelProperty("数据Id")
    private Long dataId;

    /**
     * 字段
     */
    @ApiModelProperty("字段")
    private Long field;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;
}
