package cn.stylefeng.guns.base.pojo.node;

import cn.atsoft.dasheng.model.tree.Cascader;
import lombok.Data;

import java.util.List;

@Data
public class CascaderNode implements Cascader {

    /**
     * 附加信息，一般用于存业务id
     */
    private String id;

    private String value;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    private List<CascaderNode> children;

    @Override
    public String getNodeId() {
        return id;
    }

    @Override
    public String getNodeParentId() {
        return parentId;
    }

    @Override
    public void setChildrenNodes(List childrenNodes) {
        this.children = childrenNodes;
    }
}
