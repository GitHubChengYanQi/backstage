package cn.atsoft.dasheng.orCode.model.result;

import lombok.Data;

@Data
public class ItemRequest {
    private String type;
    private OrcodeBackItem orcodeBackItem;
    private Long inKindNumber;
}
