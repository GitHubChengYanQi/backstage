package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;


@Data
public class AuditRule {

    private List<Rule> rules;     //规则

    private RuleType type;

    private Long documentsStatusId; //

    @Data
    public static class Rule {

        private DataType type;         //类型

        private List<AppointUser> appointUsers;  //指定人

        private List<DeptPosition> deptPositions;  //部门+职位

        private PurchaseAsk purchaseAsk;

    }

    @Data
    public static class PurchaseAsk {
        private String operator;

        private Integer value;
    }


}

