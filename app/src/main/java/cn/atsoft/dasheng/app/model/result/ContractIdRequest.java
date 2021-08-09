package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.List;

@Data
public class ContractIdRequest {
    private List<Long> contractId;
}
