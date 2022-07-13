package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ShopCart;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.ShopCartResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author song
 * @since 2022-06-06
 */
public interface ShopCartService extends IService<ShopCart> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-06-06
     */
    Long add(ShopCartParam param);

    List<ShopCart> sendBack(List<Long> ids);

    void addDynamic(Long fromId, String content);

    Set<String> backType(List<String> types);

    void addList(List<ShopCartParam> params);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-06-06
     */
    List<Long> delete(ShopCartParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-06-06
     */
    Long update(ShopCartParam param);

    List<ShopCartResult> allList(ShopCartParam param);

    List<ShopCartResult> merge(Long inventoryId);

    List<ShopCartResult> applyList(ShopCartParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-06-06
     */
    ShopCartResult findBySpec(ShopCartParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-06-06
     */
    List<ShopCartResult> findListBySpec(ShopCartParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-06-06
     */
     PageInfo<ShopCartResult> findPageBySpec(ShopCartParam param);

    void format(List<ShopCartResult> data);
}
