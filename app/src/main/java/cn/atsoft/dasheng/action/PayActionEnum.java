package cn.atsoft.dasheng.action;


import cn.atsoft.dasheng.action.FormActionInterface;

public enum PayActionEnum implements FormActionInterface {

    start {
        public Integer getStatus() {
            return 0;
        }
    },
    done {
        public Integer getStatus() {
            return 99;
        }
    },
    pay {
        @Override
        public Integer getStatus() {
            return null;
        }
    },
    refuse {
        public Integer getStatus() {
            return 50;
        }

    }
}

