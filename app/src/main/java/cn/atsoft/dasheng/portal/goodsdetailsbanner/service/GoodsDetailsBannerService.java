package cn.atsoft.dasheng.portal.goodsdetailsbanner.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.params.GoodsDetailsBannerParam;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.result.GoodsDetailsBannerResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品轮播图 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
public interface GoodsDetailsBannerService extends IService<GoodsDetailsBanner> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void add(GoodsDetailsBannerParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void delete(GoodsDetailsBannerParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void update(GoodsDetailsBannerParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    GoodsDetailsBannerResult findBySpec(GoodsDetailsBannerParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<GoodsDetailsBannerResult> findListBySpec(GoodsDetailsBannerParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
     PageInfo<GoodsDetailsBannerResult> findPageBySpec(GoodsDetailsBannerParam param);

}
