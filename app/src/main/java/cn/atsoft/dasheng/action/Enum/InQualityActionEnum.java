package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

public enum InQualityActionEnum implements FormActionInterface {

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

    quality_task_perform{
    }

}
