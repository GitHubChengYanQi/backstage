package cn.atsoft.dasheng.form.pojo;

public enum AuditType {
  process("审批"),
  start("开始"),
  route("路由"),
  branch("分支"),
  send("抄送");

   private final String name;

  public String getName() {
    return name;
  }

  AuditType(String name) {
    this.name = name;
  }
}
