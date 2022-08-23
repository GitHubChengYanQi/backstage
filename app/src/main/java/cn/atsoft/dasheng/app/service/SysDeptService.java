package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysDept;
import cn.atsoft.dasheng.app.model.params.SysDeptParam;
import cn.atsoft.dasheng.app.model.result.SysDeptResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author 
 * @since 2020-12-22
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 新增
     *
     * @author 
     * @Date 2020-12-22
     */
    void add(SysDeptParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2020-12-22
     */
    void delete(SysDeptParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2020-12-22
     */
    void update(SysDeptParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2020-12-22
     */
    SysDeptResult findBySpec(SysDeptParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2020-12-22
     */
    List<SysDeptResult> findListBySpec(SysDeptParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2020-12-22
     */
     PageInfo findPageBySpec(SysDeptParam param, DataScope dataScope );

}
