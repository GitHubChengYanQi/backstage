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
package cn.atsoft.dasheng.base.pojo.node;

import cn.atsoft.dasheng.model.tree.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * tree 插件的节点
 */
@Data
@ApiModel
public class TreeNode implements Tree {

    /**
     * 节点id
     */
    @ApiModelProperty("Tree组件key")
    private String key;

    @ApiModelProperty("Tree组件key")
    private String value;

    /**
     * 父节点id
     */
    @ApiModelProperty("Tree组件父级Id")
    private String parentId;

    /**
     * 节点名称
     */
    @ApiModelProperty("Tree组件名称")
    private String title;

    @ApiModelProperty("Tree组件名称")
    private String label;

    @ApiModelProperty("Tree组件子集")
    private List<TreeNode> children;


    @ApiModelProperty("排序")
    private String sort;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("object")
    private Object object;



    /**
     * 创建tree的父级节点
     */
    public static TreeNode createParent() {
        TreeNode TreeNode = new TreeNode();
        TreeNode.setKey("0");
        TreeNode.setValue("0");
        TreeNode.setTitle("顶级");
        TreeNode.setLabel("顶级");
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

        if (null == parentId) {
            return "-1";
        }
        return parentId;
    }

    @Override
    public void setChildrenNodes(List childrenNodes) {
        this.children = childrenNodes;
    }
}
