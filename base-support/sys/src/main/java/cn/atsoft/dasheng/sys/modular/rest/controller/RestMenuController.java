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
package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.core.treebuild.DefaultCascaderBuildFactory;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.sys.core.constant.dictmap.DeleteDict;
import cn.atsoft.dasheng.sys.core.constant.dictmap.MenuDict;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.core.log.LogObjectHolder;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestMenu;
import cn.atsoft.dasheng.sys.modular.rest.model.MenuQueryParam;
import cn.atsoft.dasheng.sys.modular.rest.model.MenuTreeNode;
import cn.atsoft.dasheng.sys.modular.system.factory.CascaderFactory;
import cn.atsoft.dasheng.sys.modular.system.model.MenuDto;
import cn.atsoft.dasheng.sys.modular.system.warpper.MenuWrapper;
import cn.hutool.core.bean.BeanUtil;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.factory.MenuFactory;
import cn.atsoft.dasheng.sys.modular.rest.service.RestMenuService;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author fengshuonan
 * @Date 2017年2月12日21:59:14
 */
@RestController
@RequestMapping("/rest/menu")
public class RestMenuController extends BaseController {

    @Autowired
    private RestMenuService restMenuService;

    @Autowired
    private RestUserService restUserService;

    /**
     * 修该菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改菜单", key = "name", dict = MenuDict.class)
    public ResponseData edit(@RequestBody MenuDto menu) {
        if (ToolUtil.isNotEmpty(menu.getMiniapp()) && menu.getMiniapp().equals(2)){
            menu.setType(2);
        }
        //如果修改了编号，则该菜单的子菜单也要修改对应编号
        this.restMenuService.updateMenu(menu);

        //刷新当前用户菜单
        this.restUserService.refreshCurrentUser();

        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/list")
    public PageInfo list(@RequestBody(required = false) MenuQueryParam menuQueryParam) {
        String menuName = "", level = "";
        Long menuId = 0L;
        if (ToolUtil.isNotEmpty(menuQueryParam)) {
            menuName = menuQueryParam.getMenuName();
            level = menuQueryParam.getLevel();
            menuId = menuQueryParam.getMenuId();
        }
        Page<Map<String, Object>> menus = this.restMenuService.selectMenus(menuName, level, menuId);
        Page<Map<String, Object>> wrap = new MenuWrapper(menus).wrap();
        return PageFactory.createPageInfo(wrap);
    }

    /**
     * 获取菜单列表（树形）
     *
     * @author fengshuonan
     * @Date 2019年2月23日22:01:47
     */
    @RequestMapping(value = "/listTree")
    public LayuiPageInfo listTree(@RequestBody MenuQueryParam menuQueryParam) {
        List<Map<String, Object>> menus = this.restMenuService.selectMenuTree(menuQueryParam.getMenuName(), menuQueryParam.getLevel());
        List<Map<String, Object>> menusWrap = new MenuWrapper(menus).wrap();

        //构建树
        List<MenuTreeNode> menuTreeNodes = MenuFactory.buildTreeNodes(menusWrap);

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(menuTreeNodes);
        return result;
    }

    /**
     * 新增菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "菜单新增", key = "name", dict = MenuDict.class)
    public ResponseData add(@RequestBody MenuDto menu) {
        if (ToolUtil.isNotEmpty(menu.getMiniapp()) && menu.getMiniapp().equals(2)){
            menu.setType(2);
        }
        this.restMenuService.addMenu(menu);
        return SUCCESS_TIP;
    }

    /**
     * 删除菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除菜单", key = "menuId", dict = DeleteDict.class)
    public ResponseData remove(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(menuId));

        this.restMenuService.delMenuContainSubMenus(menuId);

        return SUCCESS_TIP;
    }

    /**
     * 查看菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/view")
    public ResponseData view(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        RestMenu menu = this.restMenuService.getById(menuId);
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(menu);
        stringObjectMap.put("miniapp",0);

        if  (menu.getType().equals("2")){

            stringObjectMap.put("miniapp",2);
        }
        return ResponseData.success(stringObjectMap);
    }

    /**
     * 获取菜单信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/getMenuInfo")
    public ResponseData getMenuInfo(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        RestMenu menu = this.restMenuService.getById(menuId);

        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(menu, menuDto);

        //设置pid和父级名称
        menuDto.setPid(ConstantFactory.me().getMenuIdByCode(menuDto.getPcode()));
        menuDto.setPcodeName(ConstantFactory.me().getMenuNameByCode(menuDto.getPcode()));

        return ResponseData.success(menuDto);
    }

    /**
     * 获取菜单列表
     */
    @RequestMapping(value = "/menuTreeList")
    public ResponseData menuTreeList() {

        List<CascaderNode> menuTreeList = this.restMenuService.menuTreeList();

        menuTreeList.add(CascaderFactory.createRoot());
        //构建树
        DefaultCascaderBuildFactory<CascaderNode> factory = new DefaultCascaderBuildFactory<>();
        factory.setRootParentId("0");
        List<CascaderNode> results = factory.doTreeBuild(menuTreeList);

        //把子节点为空的设为null
        MenuWrapper.clearNull(results);

        return ResponseData.success(results);
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     */
    @RequestMapping(value = "/selectMenuTreeList")
    public ResponseData selectMenuTreeList() {
        List<CascaderNode> roleTreeList = this.restMenuService.menuTreeList();

        roleTreeList.add(CascaderFactory.createRoot());
        //构建树
        DefaultCascaderBuildFactory<CascaderNode> factory = new DefaultCascaderBuildFactory<>();
        factory.setRootParentId("-1");
        List<CascaderNode> results = factory.doTreeBuild(roleTreeList);

        //把子节点为空的设为null
        MenuWrapper.clearNull(results);

        return ResponseData.success(results);
    }

    /**
     * 获取角色的菜单列表
     */
    @RequestMapping(value = "/menuTreeListByRoleId")
    public ResponseData menuTreeListByRoleId(@RequestParam Long roleId) {
        List<String> menuIds = this.restMenuService.getMenuIdsByRoleId(roleId);
        Map<String,Object> result = new HashMap<>();
        result.put("checked",menuIds);
        return ResponseData.success(result);
//        if (ToolUtil.isEmpty(menuIds)) {
//            return this.restMenuService.menuTree();
//        } else {
//            return this.restMenuService.menuTreeListByMenuIds(menuIds);
//        }
    }

    @RequestMapping(value = "/menuTree")
    public ResponseData menuTree() {
        List<TreeNode> menuTreeList = this.restMenuService.menuTree();
        menuTreeList.add(TreeNode.createParent());
        DefaultTreeBuildFactory<TreeNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("-1");
        List<TreeNode> results = factory.doTreeBuild(menuTreeList);
        return ResponseData.success(results);
    }

}
