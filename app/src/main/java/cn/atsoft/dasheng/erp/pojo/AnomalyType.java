package cn.atsoft.dasheng.erp.pojo;

/**
 * 异常类型枚举
 */
public enum AnomalyType {

    StocktakingError("盘点异常"),
    timelyInventory("即时盘点异常"),
    allStocktaking("合并盘点异常"),
    InstockError("入库异常");


    private String name;

    AnomalyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
