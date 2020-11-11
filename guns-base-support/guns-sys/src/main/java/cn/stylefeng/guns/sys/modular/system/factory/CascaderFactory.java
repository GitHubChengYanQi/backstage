package cn.stylefeng.guns.sys.modular.system.factory;

import cn.stylefeng.guns.base.pojo.node.CascaderNode;
import cn.stylefeng.guns.base.pojo.node.LayuiTreeNode;

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
