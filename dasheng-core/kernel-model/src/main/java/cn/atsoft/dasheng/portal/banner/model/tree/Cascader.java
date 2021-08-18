package cn.atsoft.dasheng.portal.banner.model.tree;

import java.util.List;

public interface Cascader {
    /**
     * 获取节点id
     */
    String getNodeId();

    String getValue();

    /**
     * 获取节点父id
     */
    String getNodeParentId();

    List getNodeParentIds();

    void setNodeParentIds(List parentIds);
    /**
     * 设置children
     */
    void setChildrenNodes(List childrenNodes);
}
