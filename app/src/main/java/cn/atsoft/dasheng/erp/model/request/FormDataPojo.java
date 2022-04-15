package cn.atsoft.dasheng.erp.model.request;

import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.params.FormDataValueParam;
import lombok.Data;

import java.util.List;

@Data
public class FormDataPojo {
    private String module;//所属模块
    private Long qrCodeId;//二维码
    private Long planId;
    private Long qualityTaskId;
    private Long skuId;
    private Long productionTaskId;
    private List<DataValueRequestParam> dataValueParams;

}
