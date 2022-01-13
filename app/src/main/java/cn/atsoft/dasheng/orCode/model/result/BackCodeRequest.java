package cn.atsoft.dasheng.orCode.model.result;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BackCodeRequest {
    private Long codeId;
    private String source;
    private Long Id;
    private Long instockOrderId;
    private Integer costPrice;
    private Integer sellingPrice;
    private Long brandId;
    private Long storehousePositionsId;
    private Long number;
    private String inkindType;
    private Long sourceId;
}
