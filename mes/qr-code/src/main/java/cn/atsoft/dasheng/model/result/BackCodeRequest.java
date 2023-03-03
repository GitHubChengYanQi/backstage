package cn.atsoft.dasheng.model.result;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class
BackCodeRequest {
    private Long codeId;
    private String source;
    private Long Id;
    private Long instockOrderId;
    private Integer costPrice;
    private Integer sellingPrice;
    private Long brandId;
    private Long storehousePositionsId;
    @Min(0)
    private Long number;
    private String inkindType;
    private Long sourceId;
    private Long customerId;
}
