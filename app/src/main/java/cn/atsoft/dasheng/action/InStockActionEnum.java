package cn.atsoft.dasheng.action;

public enum InStockActionEnum implements FormActionInterface {

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
