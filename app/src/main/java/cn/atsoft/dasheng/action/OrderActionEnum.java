package cn.atsoft.dasheng.action;

public enum OrderActionEnum  implements FormActionInterface{

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
    refuse {
        public Integer getStatus() {
            return 50;
        }

    }
}
