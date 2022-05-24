package cn.atsoft.dasheng.action.Enum;

public enum ReceiptsEnum {

    PURCHASE("采购申请单")
    ,
    PURCHASEORDER("采购单")
    ,
    INSTOCK("入库单")
    ,
    INSTOCKERROR("入库异常")
    ,
    OUTSTOCK("出库单")
    ,
    QUALITY("质检")
    ;

    private String value;

    ReceiptsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
