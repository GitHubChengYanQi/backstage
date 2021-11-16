package cn.atsoft.dasheng.form.model.params;

import lombok.Data;

@Data
public class DataRequest {
    private Long formId; //表单id
    private String module;//所属模块
    private Long field; // 比较字段
    private String value;//值
}
