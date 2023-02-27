package cn.atsoft.dasheng.outStock.model.enums;

import cn.atsoft.dasheng.audit.service.impl.RestFormActionInterface;

public enum OutStockActionEnum implements RestFormActionInterface {

    start("开始") {
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
    resubmit("再次提交") {  //再次提交

    },
    outStock("出库") {

    };

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    OutStockActionEnum(String value) {
        this.value = value;
    }
}
