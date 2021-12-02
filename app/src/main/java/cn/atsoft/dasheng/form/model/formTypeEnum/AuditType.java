package cn.atsoft.dasheng.form.model.formTypeEnum;

public enum AuditType {
    /**
     * 定义类型
     */
    START("start"),
    AUDIT("audit"),
    SEND("send"),
    ROUTE("route"),
    BRANCH("branch");

    private String value;

    AuditType(String value) {
        this.value = value;
    }


}
