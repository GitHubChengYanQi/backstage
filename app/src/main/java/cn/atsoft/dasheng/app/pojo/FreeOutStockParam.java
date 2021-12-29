package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FreeOutStockParam {
    @NotNull
    private Long codeId;
    @NotNull
    private Long number;
}
