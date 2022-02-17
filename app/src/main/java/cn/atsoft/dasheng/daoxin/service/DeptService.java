package cn.atsoft.dasheng.daoxin.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.Dept;
import cn.atsoft.dasheng.daoxin.model.params.DeptParam;
import cn.atsoft.dasheng.daoxin.model.result.DeptResult;
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
public interface DeptService extends IService<Dept> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void add(DeptParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void delete(DeptParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    void update(DeptParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    DeptResult findBySpec(DeptParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    List<DeptResult> findListBySpec(DeptParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
     PageInfo<DeptResult> findPageBySpec(DeptParam param);

}
