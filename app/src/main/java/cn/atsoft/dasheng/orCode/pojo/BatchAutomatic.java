package cn.atsoft.dasheng.orCode.pojo;

import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import lombok.Data;

import java.util.List;

@Data
public class BatchAutomatic {
    private List<BackCodeRequest> codeRequests;

}
