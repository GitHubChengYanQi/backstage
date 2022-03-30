package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllBomResult {
    private List<BomOrder> result ;
    private List<SkuResult> owe ;
}
