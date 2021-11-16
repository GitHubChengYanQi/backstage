package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

import java.util.List;

@Data
public class FormDataPojo {
    private String module;//所属模块
    private Long formId;//表单id
  private List<FormValues> formValues;
    private Integer option;
}
