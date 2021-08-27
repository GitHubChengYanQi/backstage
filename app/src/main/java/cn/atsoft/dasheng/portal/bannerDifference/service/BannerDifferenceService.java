package cn.atsoft.dasheng.portal.bannerDifference.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.bannerDifference.entity.BannerDifference;
import cn.atsoft.dasheng.portal.bannerDifference.model.params.BannerDifferenceParam;
import cn.atsoft.dasheng.portal.bannerDifference.model.result.BannerDifferenceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 轮播图分类 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
public interface BannerDifferenceService extends IService<BannerDifference> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-18
     */
    void add(BannerDifferenceParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-18
     */
    void delete(BannerDifferenceParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-18
     */
    void update(BannerDifferenceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
    BannerDifferenceResult findBySpec(BannerDifferenceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
    List<BannerDifferenceResult> findListBySpec(BannerDifferenceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
     PageInfo<BannerDifferenceResult> findPageBySpec(BannerDifferenceParam param);

}
