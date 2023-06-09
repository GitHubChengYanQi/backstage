package cn.atsoft.dasheng.sys.modular.rest.service;

import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.constant.state.MenuStatus;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.core.listener.ConfigListener;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestMenu;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestMenuMapper;
import cn.atsoft.dasheng.sys.modular.system.model.MenuDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.base.pojo.node.MenuNode;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class RestMenuService extends ServiceImpl<RestMenuMapper, RestMenu> {

    @Resource
    private RestMenuMapper restMenuMapper;

    /**
     * 添加菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:59 PM
     */
    @Transactional
    public void addMenu(MenuDto menuDto) {

        if (ToolUtil.isOneEmpty(menuDto, menuDto.getCode(), menuDto.getName(), menuDto.getMenuFlag(), menuDto.getUrl(), menuDto.getSystemType())) {
            throw new RequestEmptyException();
        }

        //判断是否已经存在该编号
        String existedMenuName = ConstantFactory.me().getMenuNameByCode(menuDto.getCode());
        if (ToolUtil.isNotEmpty(existedMenuName)) {
            throw new ServiceException(BizExceptionEnum.EXISTED_THE_MENU);
        }

        //组装属性，设置父级菜单编号
        RestMenu resultMenu = this.menuSetPcode(menuDto);

        resultMenu.setStatus(MenuStatus.ENABLE.getCode());

        this.save(resultMenu);
    }

    /**
     * 更新菜单
     *
     * @author fengshuonan
     * @Date 2019/2/27 4:09 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuDto menuDto) {

        //如果菜单为空
        if (menuDto == null || ToolUtil.isOneEmpty(menuDto.getMenuId(), menuDto.getCode())) {
            throw new RequestEmptyException();
        }

        //获取旧的菜单
        Long id = menuDto.getMenuId();
        RestMenu menu = this.getById(id);

        if (menu == null) {
            throw new RequestEmptyException();
        }

        //设置父级菜单编号
        RestMenu resultMenu = this.menuSetPcode(menuDto);

        //查找该节点的子集合,并修改相应的pcodes和level(因为修改菜单后,层级可能变化了)
        updateSubMenuLevels(menu, resultMenu);

        this.updateById(resultMenu);
    }

    /**
     * 更新所有子菜单的结构
     *
     * @param oldMenu 原来的菜单
     * @param newMenu 新菜单
     * @author fengshuonan
     * @Date 2019/2/27 4:25 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSubMenuLevels(RestMenu oldMenu, RestMenu newMenu) {

        List<RestMenu> menus = restMenuMapper.getMenusLikePcodes(oldMenu.getCode());

        for (RestMenu menu : menus) {

            //更新pcode
            if (oldMenu.getCode().equals(menu.getPcode())) {
                menu.setPcode(newMenu.getCode());
            }

            //更新pcodes
            String oldPcodesPrefix = oldMenu.getPcodes() + "[" + oldMenu.getCode() + "],";
            String oldPcodesSuffix = menu.getPcodes().substring(oldPcodesPrefix.length());
            String menuPcodes = newMenu.getPcodes() + "[" + newMenu.getCode() + "]," + oldPcodesSuffix;
            menu.setPcodes(menuPcodes);

            //更新levels
            int level = StrUtil.count(menuPcodes, "[");
            menu.setLevels(level);

            //更新systemType
            menu.setSystemType(newMenu.getSystemType());

            this.updateById(menu);
        }

    }

    /**
     * 删除菜单
     *
     * @author stylefeng
     * @Date 2017/5/5 22:20
     */
    @Transactional
    public void delMenu(Long menuId) {

        //删除菜单
        this.restMenuMapper.deleteById(menuId);

        //删除关联的relation
        this.restMenuMapper.deleteRelationByMenu(menuId);
    }

    /**
     * 删除菜单包含所有子菜单
     *
     * @author stylefeng
     * @Date 2017/6/13 22:02
     */
    @Transactional
    public void delMenuContainSubMenus(Long menuId) {

        RestMenu menu = restMenuMapper.selectById(menuId);

        //删除当前菜单
        delMenu(menuId);

        //删除所有子菜单
        List<RestMenu> menus = restMenuMapper.getMenusLikePcodes(menu.getCode());

        for (RestMenu temp : menus) {
            delMenu(temp.getMenuId());
        }
    }

    /**
     * 根据条件查询菜单
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    public Page<Map<String, Object>> selectMenus(String condition, String level, Long menuId) {

        //获取menuId的code
        String code = "";
        if (menuId != null && menuId != 0L) {
            RestMenu menu = this.getById(menuId);
            code = menu.getCode();
        }

        Page page = LayuiPageFactory.defaultPage();

        return this.baseMapper.selectMenus(page, condition, level, menuId, code);
    }

    /**
     * 根据条件查询菜单
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    public List<String> getMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.getMenuIdsByRoleId(roleId);
    }

    /**
     * 获取菜单列表树
     */
    public List<CascaderNode> menuTreeList() {
        return this.baseMapper.treeviewNodes();
    }

    public List<TreeNode> menuTree() {
        return this.baseMapper.treeNodes();
    }

    /**
     * 获取菜单列表树
     */
    public List<TreeNode> menuTreeListByMenuIds(List<Long> menuIds) {
        return this.baseMapper.menuTreeListByMenuIds(menuIds);
    }

    /**
     * 删除menu关联的relation
     *
     * @param menuId
     * @return
     * @date 2017年2月19日 下午4:10:59
     */
    public int deleteRelationByMenu(Long menuId) {
        return this.baseMapper.deleteRelationByMenu(menuId);
    }

    /**
     * 获取资源url通过角色id
     *
     * @param roleId
     * @return
     * @date 2017年2月19日 下午7:12:38
     */
    public List<String> getResUrlsByRoleId(Long roleId) {
        return this.baseMapper.getResUrlsByRoleId(roleId);
    }

    /**
     * 根据角色获取菜单
     *
     * @param roleIds
     * @return
     * @date 2017年2月19日 下午10:35:40
     */
    public List<MenuNode> getMenusByRoleIds(List<Long> roleIds) {
        List<MenuNode> menus = this.baseMapper.getMenusByRoleIds(roleIds);

        //给所有的菜单url加上ctxPath
        for (MenuNode menuItem : menus) {
            menuItem.setUrl(ConfigListener.getConf().get("contextPath") + menuItem.getUrl());
        }

        return menus;
    }
    public List<MenuNode> getMobileMenusByRoleIds(List<Long> roleIds,Integer type) {
            List<MenuNode> menus = this.baseMapper.getMobileMenusByRoleIds(roleIds,type);

            //给所有的菜单url加上ctxPath
            for (MenuNode menuItem : menus) {
                menuItem.setUrl(ConfigListener.getConf().get("contextPath") + menuItem.getUrl());
            }

            return menus;
        }

    /**
     * 根据code查询菜单
     *
     * @author fengshuonan
     * @Date 2018/12/20 21:54
     */
    public RestMenu selectByCode(String code) {
        RestMenu menu = new RestMenu();
        menu.setCode(code);
        QueryWrapper<RestMenu> queryWrapper = new QueryWrapper<>(menu);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据请求的父级菜单编号设置pcode和层级
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    public RestMenu menuSetPcode(MenuDto menuParam) {

        RestMenu resultMenu = new RestMenu();
        ToolUtil.copyProperties(menuParam, resultMenu);

        if (ToolUtil.isEmpty(menuParam.getPid()) || menuParam.getPid().equals(0L)) {
            resultMenu.setPcode("0");
            resultMenu.setPcodes("[0],");
            resultMenu.setLevels(1);
            resultMenu.setPid(0L);
            resultMenu.setPids("0,");
        } else {
            Long pid = menuParam.getPid();
            RestMenu pMenu = this.getById(pid);
            Integer pLevels = pMenu.getLevels();
            resultMenu.setPcode(pMenu.getCode());
            resultMenu.setPid(pMenu.getMenuId());

            //如果编号和父编号一致会导致无限递归
            if (menuParam.getCode().equals(menuParam.getPcode())) {
                throw new ServiceException(BizExceptionEnum.MENU_PCODE_COINCIDENCE);
            }

            resultMenu.setLevels(pLevels + 1);
            resultMenu.setPcodes(pMenu.getPcodes() + "[" + pMenu.getMenuId() + "],");
            resultMenu.setPids(pMenu.getPids() + pMenu.getMenuId() + ",");
        }

        return resultMenu;
    }

    /**
     * 获取菜单树形列表
     *
     * @author fengshuonan
     * @Date 2019/2/23 22:02
     */
    public List<Map<String, Object>> selectMenuTree(String condition, String level) {
        List<Map<String, Object>> maps = this.baseMapper.selectMenuTree(condition, level);

        if (maps == null) {
            maps = new ArrayList<>();
        }

        //修复菜单查询bug，带条件的暂时先父级置为0
        if (ToolUtil.isNotEmpty(condition) || ToolUtil.isNotEmpty(level)) {
            if (maps.size() > 0) {

                //将pcode置为root
                for (Map<String, Object> menu : maps) {
                    menu.put("pcode", "0");
                }
            }
        }

        //创建根节点
        RestMenu menu = new RestMenu();
        menu.setMenuId(-1L);
        menu.setName("根节点");
        menu.setCode("0");
        menu.setPcode("-2");
        maps.add(BeanUtil.beanToMap(menu));

        return maps;
    }

}
