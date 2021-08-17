package cn.atsoft.dasheng.app.cloude.portal.service;

import cn.atsoft.dasheng.app.cloude.portal.entity.PortalBanner;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.cloude.portal.model.params.PortalBannerParam;
import cn.atsoft.dasheng.app.cloude.portal.model.result.PortalBannerResult;
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
public interface PortalBannerService extends IService<PortalBanner> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-17
     */
    void add(PortalBannerParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-17
     */
    void delete(PortalBannerParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-17
     */
    void update(PortalBannerParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-17
     */
    PortalBannerResult findBySpec(PortalBannerParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-17
     */
    List<PortalBannerResult> findListBySpec(PortalBannerParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-17
     */
     PageInfo<PortalBannerResult> findPageBySpec(PortalBannerParam param);

}
