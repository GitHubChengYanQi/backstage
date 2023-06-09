package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum AllocationActionEnum   implements FormActionInterface {
    start ("开始"){
        public Long getStatus() {
            return 0L;
        }
    },
    done("完成") {
        public Long getStatus() {
            return 99L;
        }
    },
    refuse("拒绝") {
        public Long getStatus() {
            return 50L;
        }
    },
    revoke("撤回") {
        public Long getStatus() {
            return 49L;
        }
    },
    assign ("分派"),
     resubmit("再次提交") {  //再次提交

    },
    carryAllocation ("执行");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    AllocationActionEnum(String value) {
        this.value = value;
    }
}
