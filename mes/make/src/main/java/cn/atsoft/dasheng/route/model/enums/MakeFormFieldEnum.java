package cn.atsoft.dasheng.route.model.enums;

public enum MakeFormFieldEnum {

    RouteSkuSelect("流程审批操作 并签或签 按钮"),
    RouteInputSkuShow("流程审批操作 并签或签 按钮"),
    RouteAbout("流程审批操作 并签或签 按钮");

    private final String name;

    public String getName() {
        return name;
    }

    MakeFormFieldEnum(String name) {
        this.name = name;
    }
}
