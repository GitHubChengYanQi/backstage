package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FreeOutStockParam {
    @NotNull
    private Long inkindId;
    @NotNull
    private Long number;

    private String type;

}
