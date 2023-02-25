package cn.atsoft.dasheng.action.Enum;

public enum ReceiptsEnum {


    PURCHASE("采购申请单")
    ,
    PURCHASEORDER("采购单")
    ,
    INSTOCK("入库单")
    ,
    INSTOCKERROR("入库异常"),
    PURCHASEASK("入库异常123")
    ,
    OUTSTOCK("出库单")
    ,
    QUALITY("质检")
    ,
    MAINTENANCE("养护")
,
    Stocktaking("盘点"),
    ERROR("错误"),
    ALLOCATION("调拨"),
    ProductionTask("生产任务");


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
