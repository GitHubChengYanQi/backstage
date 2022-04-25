package cn.atsoft.dasheng.action;


import cn.atsoft.dasheng.action.FormActionInterface;

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
    pay {

    }
}

