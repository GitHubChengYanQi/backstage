package cn.atsoft.dasheng.orCode.model.result;

import lombok.Data;

@Data
public class BackCodeRequest {
    private Long id;
    private String source;
    private Long spuId;
}
