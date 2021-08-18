package cn.atsoft.dasheng.protal.classPage.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.protal.classPage.entity.DaoxinPortalClass;
import cn.atsoft.dasheng.protal.classPage.model.params.DaoxinPortalClassParam;
import cn.atsoft.dasheng.protal.classPage.model.result.DaoxinPortalClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类导航 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface DaoxinPortalClassService extends IService<DaoxinPortalClass> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void add(DaoxinPortalClassParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void delete(DaoxinPortalClassParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void update(DaoxinPortalClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    DaoxinPortalClassResult findBySpec(DaoxinPortalClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<DaoxinPortalClassResult> findListBySpec(DaoxinPortalClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
     PageInfo<DaoxinPortalClassResult> findPageBySpec(DaoxinPortalClassParam param);

}
