package cn.atsoft.dasheng.action.Enum;

public enum ProductionQualityActionEnum {

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
    },
}
