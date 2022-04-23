package cn.atsoft.dasheng.action;

public enum QualityActionEnum  implements FormActionInterface {

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
