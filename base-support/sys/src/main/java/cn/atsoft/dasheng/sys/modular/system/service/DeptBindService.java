package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.DeptBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.DeptBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DeptBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 各租户内 部门绑定表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-04
 */
public interface DeptBindService extends IService<DeptBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    void add(DeptBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    void delete(DeptBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    void update(DeptBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    DeptBindResult findBySpec(DeptBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    List<DeptBindResult> findListBySpec(DeptBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
     PageInfo<DeptBindResult> findPageBySpec(DeptBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
     PageInfo<DeptBindResult> findPageBySpec(DeptBindParam param,DataScope dataScope);

    void format(List<DeptBindResult> dataList);
}
