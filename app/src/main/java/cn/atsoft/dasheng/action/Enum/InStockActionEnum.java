package cn.atsoft.dasheng.action.Enum;

import cn.atsoft.dasheng.action.service.FormActionInterface;

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
    ,
    verify{  //核实

    }
    ,performInstock{  //执行入库

    }
    ,

}
