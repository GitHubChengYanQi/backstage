package cn.atsoft.dasheng.daoxin.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.DaoxinDept;
import cn.atsoft.dasheng.daoxin.model.params.DaoxinDeptParam;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinDeptResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * daoxin部门表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
public interface DaoxinDeptService extends IService<DaoxinDept> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void add(DaoxinDeptParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void delete(DaoxinDeptParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void update(DaoxinDeptParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    DaoxinDeptResult findBySpec(DaoxinDeptParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    List<DaoxinDeptResult> findListBySpec(DaoxinDeptParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
     PageInfo<DaoxinDeptResult> findPageBySpec(DaoxinDeptParam param);

}
