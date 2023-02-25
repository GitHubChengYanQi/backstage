package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class AuditRule implements Serializable{

    private List<Rule> rules;     //规则

    private RuleType type;

    private Long documentsStatusId; //

    private String formType;

    private List<ActionStatus> actionStatuses;

    //1或签 2并签
    private Integer nodeApprovalType;



    @Data
    public static class Rule implements Serializable{

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

