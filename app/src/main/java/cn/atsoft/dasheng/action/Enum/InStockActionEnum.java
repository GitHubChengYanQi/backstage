package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum InStockActionEnum implements FormActionInterface {

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
    resubmit("再次提交") {  //再次提交

    },
    revoke("撤回") {
        public Long getStatus() {
            return 49L;
        }
    },
    verify("核实") {  //核实

    }, performInstock("执行入库") {  //执行入库

    };

    private String value;

    InStockActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
