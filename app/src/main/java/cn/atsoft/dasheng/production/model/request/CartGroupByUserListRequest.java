package cn.atsoft.dasheng.production.model.request;

import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import lombok.Data;

import java.util.List;

@Data
public class CartGroupByUserListRequest {
    private Long userId;
    private String name;
    private List<ProductionPickListsCartResult> cartResults;

}
