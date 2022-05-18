package cn.atsoft.dasheng.action.Enum;


import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum PayActionEnum implements FormActionInterface {

    start {
        public Long getStatus() {
            return 0L;
        }
    },
    done {
        public Long getStatus() {
            return 99L;
        }
    },
    refuse {
        public Long getStatus() {
            return 50L;
        }
    }
}

