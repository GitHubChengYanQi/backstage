package cn.atsoft.dasheng.inventory.pojo;

import lombok.Data;

import java.util.List;

@Data
public class InventoryRequest {

    private List<InkindParam> inkindParams;

    @Data
    public class InkindParam {
        private Long inkindId;
        private Long number;
    }
}
