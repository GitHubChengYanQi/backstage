package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum SoOrderActionEnum implements FormActionInterface {

    start("开始") {
        public Long getStatus() {
            return 0L;
        }
    },
    done("完成") {
        public Long getStatus() {
            return 99L;
        }
    },   resubmit("再次提交") {  //再次提交

    },
    refuse("拒绝") {
        public Long getStatus() {
            return 50L;
        }
    };


    SoOrderActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
}
