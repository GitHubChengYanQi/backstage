package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.List;

@Data
public class BatchDeleteRequest {
    private List<Long> ids;
}
