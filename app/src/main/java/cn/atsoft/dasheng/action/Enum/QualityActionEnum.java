package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum QualityActionEnum implements FormActionInterface {

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
    revoke("撤回") {
        public Long getStatus() {
            return 49L;
        }
    },
    refuse("拒绝") {
        public Long getStatus() {
            return 50L;
        }
    };


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;


    QualityActionEnum(String value) {
        this.value = value;
    }
}
