package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

@Data
public class FormDataPojo {
    private String module;//所属模块
    private Long formId;//表单id
    private Long field; //字段
    private String value;//值
}
