package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.MenuConfig;
import cn.atsoft.dasheng.sys.modular.system.model.params.MenuConfigParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.MenuConfigResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单显示配置表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-28
 */
public interface MenuConfigService extends IService<MenuConfig> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    void add(MenuConfigParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    void delete(MenuConfigParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    void update(MenuConfigParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    MenuConfigResult findBySpec(MenuConfigParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    List<MenuConfigResult> findListBySpec(MenuConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
     PageInfo<MenuConfigResult> findPageBySpec(MenuConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
     PageInfo<MenuConfigResult> findPageBySpec(MenuConfigParam param,DataScope dataScope);

}
