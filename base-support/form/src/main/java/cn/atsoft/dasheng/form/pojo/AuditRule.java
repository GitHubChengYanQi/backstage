package cn.atsoft.dasheng.form.pojo;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.List;


@Data
public class AuditRule {

    private List<Rule> rules;     //规则

    private RuleType type;

    @Data
    public class Rule<T> {

        private DataType type;         //类型

        private List<T> data;

    }

}

