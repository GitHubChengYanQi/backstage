package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ItemData;
import cn.atsoft.dasheng.crm.model.params.ItemDataParam;
import cn.atsoft.dasheng.crm.model.result.ItemDataResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品资料 服务类
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
public interface ItemDataService extends IService<ItemData> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-09-11
     */
    void add(ItemDataParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-09-11
     */
    void delete(ItemDataParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-09-11
     */
    void update(ItemDataParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-09-11
     */
    ItemDataResult findBySpec(ItemDataParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-09-11
     */
    List<ItemDataResult> findListBySpec(ItemDataParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-09-11
     */
     PageInfo<ItemDataResult> findPageBySpec(ItemDataParam param);

}
