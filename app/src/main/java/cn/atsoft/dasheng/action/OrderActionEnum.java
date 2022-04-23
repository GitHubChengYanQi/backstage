package cn.atsoft.dasheng.action;

public enum OrderActionEnum {

    start {
        public Long getStatus() {
            return 0L;
        }
    },
    done {
        public Long getStatus() {
            return 99L;
        }
    }


}
