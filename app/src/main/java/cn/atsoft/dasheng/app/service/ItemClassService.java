package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemClass;
import cn.atsoft.dasheng.app.model.params.ItemClassParam;
import cn.atsoft.dasheng.app.model.result.ItemClassResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品分类表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
public interface ItemClassService extends IService<ItemClass> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void add(ItemClassParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void delete(ItemClassParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-11
     */
    void update(ItemClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    ItemClassResult findBySpec(ItemClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<ItemClassResult> findListBySpec(ItemClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-11
     */
    PageInfo findPageBySpec(ItemClassParam param, DataScope dataScope );


}
