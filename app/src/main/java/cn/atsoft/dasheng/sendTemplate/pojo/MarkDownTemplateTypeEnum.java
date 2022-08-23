package cn.atsoft.dasheng.sendTemplate.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum
MarkDownTemplateTypeEnum {
    audit("审批申请"),
    send("审批抄送"),
    action("您有${functionName}任务待执行"),
    refuse("审批被驳回"),
    done("您的${functionName}申请已完成"),
    atPerson("${userName}@了你"),
    toPerson("${userName}发布了评论"),
    pickSend("您有物料可领取"),
    forward("${userName}转交的异常处理请求"),
    warning("物料不足"),
    transfer("有物料需要您进行库内调拨");

    @EnumValue
    private String enumName;

    @Override
    public String toString() {
        return "ProcessModuleEnum{" + "name='" + enumName + '\'' + '}';
    }

    public String getEnumName() {
        return enumName;
    }

    MarkDownTemplateTypeEnum(String enumName) {
        this.enumName = enumName;
    }

}
