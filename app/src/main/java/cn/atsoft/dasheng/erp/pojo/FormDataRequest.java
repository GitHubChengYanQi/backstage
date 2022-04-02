package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.form.entity.FormData;
import lombok.Data;

import java.util.List;

@Data
public class FormDataRequest {
    private FormData data;
    private List<FormDataValueResult> dataValueResults;
    private SkuResult skuResult;
}
