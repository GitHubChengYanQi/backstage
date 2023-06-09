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
package cn.atsoft.dasheng.sys.modular.system.warpper;

import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.atsoft.dasheng.model.enums.YesOrNotEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 菜单列表的包装类
 *
 * @author fengshuonan
 * @date 2017年2月19日15:07:29
 */
public class MenuWrapper extends BaseControllerWrapper {

    public MenuWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public MenuWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getMenuStatusName((String) map.get("status")));

        String menuFlag = (String) map.get("menuFlag");
        for (YesOrNotEnum value : YesOrNotEnum.values()) {
            if (value.name().equals(menuFlag)) {
                map.put("isMenuName", value.getDesc());
            }
        }

        //删除虚拟的父节点
        if (map.get("pcode").equals("0")) {
            map.remove("pcode");
        }

    }

    public static void clearNull(List<CascaderNode> list) {
        if (list == null) {
            return;
        } else {
            if (list.size() == 0) {
                return;
            } else {
                for (CascaderNode node : list) {
                    if (node.getChildren() != null) {
                        if (node.getChildren().size() == 0) {
                            node.setChildren(null);
                        } else {
                            clearNull(node.getChildren());
                        }
                    }
                }
            }
        }
    }
}
