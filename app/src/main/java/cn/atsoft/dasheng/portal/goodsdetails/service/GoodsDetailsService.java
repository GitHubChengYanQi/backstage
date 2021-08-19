package cn.atsoft.dasheng.portal.goodsdetails.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goodsdetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsdetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsdetails.model.result.GoodsDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页商品详情 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
public interface GoodsDetailsService extends IService<GoodsDetails> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void add(GoodsDetailsParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void delete(GoodsDetailsParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    void update(GoodsDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    GoodsDetailsResult findBySpec(GoodsDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<GoodsDetailsResult> findListBySpec(GoodsDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-19
     */
     PageInfo<GoodsDetailsResult> findPageBySpec(GoodsDetailsParam param);

}
