package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;


@Data
public class AuditRule {

    private List<Rule> rules;     //规则

    private RuleType type;

    @Data
    public static class Rule {

        private DataType type;         //类型

        private List<AppointUser> appointUsers;  //指定人

        private List<DeptPosition> deptPositions;  //部门+职位


    }



}

