package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import lombok.Data;

import java.util.List;

@Data
public class OrcodeBackItem {
    private List<BackSku> backSkus;
    private SpuResult backSpu;
    private String skuName;
}
