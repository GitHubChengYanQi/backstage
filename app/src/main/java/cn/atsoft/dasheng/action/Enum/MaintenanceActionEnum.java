package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum MaintenanceActionEnum implements FormActionInterface {
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

    maintenanceing ("开始养护");
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    MaintenanceActionEnum(String value) {
        this.value = value;
    }
}
