package cn.atsoft.dasheng.orCode.model.result;

import lombok.Data;

import java.util.List;

@Data
public class OrCodeRequest {
    private List<Long> ids;
    private String type;
}
