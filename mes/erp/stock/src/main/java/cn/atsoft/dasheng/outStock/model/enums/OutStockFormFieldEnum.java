package cn.atsoft.dasheng.outStock.model.enums;

public enum OutStockFormFieldEnum {
    orderStatus("单据状态");

    private final String name;

    public String getName() {
        return name;
    }

    OutStockFormFieldEnum(String name) {
        this.name = name;
    }
}
