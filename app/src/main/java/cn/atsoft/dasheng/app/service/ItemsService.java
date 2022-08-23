package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 物品表 服务类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface ItemsService extends IService<Items> {


    /**
     * 新增
     *
     * @author 1
     * @Date 2021-07-14
     */
    Long add(ItemsParam param);

    /**
     * 删除
     *
     * @author 1
     * @Date 2021-07-14
     */
    void delete(ItemsParam param);

    /**
     * 更新
     *
     * @author 1
     * @Date 2021-07-14
     */
    void update(ItemsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    ItemsResult findBySpec(ItemsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<ItemsResult> findListBySpec(ItemsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
     PageInfo findPageBySpec(ItemsParam param, DataScope dataScope );

    void formatResult(ItemsResult data);
    void batchDelete(List<Long> ids);
}
