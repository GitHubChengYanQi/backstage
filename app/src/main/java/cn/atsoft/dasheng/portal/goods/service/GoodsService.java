package cn.atsoft.dasheng.portal.goods.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.model.params.GoodsParam;
import cn.atsoft.dasheng.portal.goods.model.result.GoodsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页商品 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void add(GoodsParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void delete(GoodsParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void update(GoodsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    GoodsResult findBySpec(GoodsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<GoodsResult> findListBySpec(GoodsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
     PageInfo<GoodsResult> findPageBySpec(GoodsParam param);

}
