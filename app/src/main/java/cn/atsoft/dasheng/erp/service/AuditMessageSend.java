package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.RuleType;

import java.util.List;

public interface AuditMessageSend {
    void send(Long taskId,RuleType type, List<Long> users,String url,String createName);
}
