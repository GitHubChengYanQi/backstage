package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.pojo.AuditType;

import java.util.List;

public interface ProcessInterface {
    List<FormField> getProcessForm(AuditType key);
}
