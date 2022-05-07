package cn.atsoft.dasheng.erp.pojo;

/**
 * 异常类型枚举
 */
public enum AnomalyType {

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
