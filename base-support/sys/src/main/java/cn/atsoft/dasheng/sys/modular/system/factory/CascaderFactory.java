package cn.atsoft.dasheng.sys.modular.system.factory;

import cn.atsoft.dasheng.base.pojo.node.CascaderNode;

public class CascaderFactory {
    public static CascaderNode createRoot() {
        CascaderNode treeNode = new CascaderNode();
        treeNode.setId("0");
        treeNode.setValue("0");
        treeNode.setLabel("顶级");
        treeNode.setParentId("-1");
        return treeNode;
    }
}
