package cn.atsoft.dasheng.action.Enum;

public enum ReceiptsEnum {

    purchaseAsk("采购申请单")
    ,
    purchaseOrder("采购单")
    ,
    createInstock("入库单")
    ,
    PO("采购单")
    ,
    SO("销售单")
    ,
    instockError("入库异常")
    ,
    outstock("领料单")
    ,
    payAsk("付款申请单")
    ,
    inQuality("入场检")
    ,
    outQuality("出厂检")
    ,
    productionQuality("生产检查")
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
