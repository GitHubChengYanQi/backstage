package cn.atsoft.dasheng.model.tree;

import java.util.List;

public interface Cascader {
    /**
     * 获取节点id
     */
    String getNodeId();

    /**
     * 获取节点父id
     */
    String getNodeParentId();

    /**
     * 设置children
     */
    void setChildrenNodes(List childrenNodes);
}
