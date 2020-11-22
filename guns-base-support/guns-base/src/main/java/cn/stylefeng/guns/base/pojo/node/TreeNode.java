/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.base.pojo.node;

import cn.atsoft.dasheng.model.tree.Tree;
import lombok.Data;

import java.util.List;

/**
 * tree 插件的节点
 */
@Data
public class TreeNode implements Tree {

    /**
     * 节点id
     */
    private String key;

    private String value;

    /**
     * 父节点id
     */
    private String parentId;

    /**
     * 节点名称
     */
    private String title;


    private List<TreeNode> children;


    /**
     * 创建ztree的父级节点
     */
    public static TreeNode createParent() {
        TreeNode TreeNode = new TreeNode();
        TreeNode.setKey("0");
        TreeNode.setTitle("顶级");
        TreeNode.setParentId("-1");
        return TreeNode;
    }

    public String getValue() {
        return key;
    }

    @Override
    public String getNodeId() {
        return key;
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
