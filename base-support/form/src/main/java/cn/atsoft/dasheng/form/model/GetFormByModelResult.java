package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.pojo.AuditType;
import lombok.Data;

import java.util.List;
@Data
public class GetFormByModelResult {

    private AuditType key;

    private String title;

    private List<FormField> fields;
}
