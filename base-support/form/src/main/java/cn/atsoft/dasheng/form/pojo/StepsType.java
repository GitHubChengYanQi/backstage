package cn.atsoft.dasheng.form.pojo;

public enum StepsType {
    /**
     * 定义类型
     */
    START("0"),
    AUDIT("1"),
    SEND("2"),
    ROUTE("3"),
    BRANCH("4");

    private final String key;

    private StepsType(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }


}
