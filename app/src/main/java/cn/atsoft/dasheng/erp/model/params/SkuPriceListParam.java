package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SkuPriceListParam {
    @NotEmpty
    List<Long> skuIds;
}