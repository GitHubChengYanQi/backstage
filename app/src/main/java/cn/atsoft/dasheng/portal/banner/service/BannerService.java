package cn.atsoft.dasheng.portal.banner.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.model.result.BannerResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 轮播图 服务类
 * </p>
 *
 * @author
 * @since 2021-08-17
 */
public interface BannerService extends IService<Banner> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-08-17
     */
    void add(BannerParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-08-17
     */
    void delete(BannerParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-08-17
     */
    void update(BannerParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-08-17
     */
    BannerResult findBySpec(BannerParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-08-17
     */
    List<BannerResult> findListBySpec(BannerParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-08-17
     */
    PageInfo<BannerResult> findPageBySpec(BannerParam param);

    void BatchDelete(List<Long> Ids);

}
