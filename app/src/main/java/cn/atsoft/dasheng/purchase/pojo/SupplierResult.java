package cn.atsoft.dasheng.purchase.pojo;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import lombok.Data;

import java.util.List;

@Data
public class SupplierResult {
    private CustomerResult customerResult;
    private List<BrandResult> brandResults;
}
