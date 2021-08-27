package cn.atsoft.dasheng.portal.navigationDifference.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.navigationDifference.entity.NavigationDifference;
import cn.atsoft.dasheng.portal.navigationDifference.model.params.NavigationDifferenceParam;
import cn.atsoft.dasheng.portal.navigationDifference.model.result.NavigationDifferenceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 导航分类 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
public interface NavigationDifferenceService extends IService<NavigationDifference> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-18
     */
    void add(NavigationDifferenceParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-18
     */
    void delete(NavigationDifferenceParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-18
     */
    void update(NavigationDifferenceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
    NavigationDifferenceResult findBySpec(NavigationDifferenceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
    List<NavigationDifferenceResult> findListBySpec(NavigationDifferenceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
     PageInfo<NavigationDifferenceResult> findPageBySpec(NavigationDifferenceParam param);

}
