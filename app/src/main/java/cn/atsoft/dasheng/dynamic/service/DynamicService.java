package cn.atsoft.dasheng.dynamic.service;

import cn.atsoft.dasheng.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.dynamic.model.result.DynamicResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-10
 */
public interface DynamicService extends IService<Dynamic> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    void add(DynamicParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    void delete(DynamicParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    void update(DynamicParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    DynamicResult findBySpec(DynamicParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    List<DynamicResult> findListBySpec(DynamicParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
     PageInfo<DynamicResult> findPageBySpec(DynamicParam param);

}
