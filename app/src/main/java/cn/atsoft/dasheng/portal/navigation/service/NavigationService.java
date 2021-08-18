package cn.atsoft.dasheng.portal.navigation.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.navigation.entity.Navigation;
import cn.atsoft.dasheng.portal.navigation.model.params.NavigationParam;
import cn.atsoft.dasheng.portal.navigation.model.result.NavigationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 导航表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
public interface NavigationService extends IService<Navigation> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-18
     */
    void add(NavigationParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-18
     */
    void delete(NavigationParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-18
     */
    void update(NavigationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
    NavigationResult findBySpec(NavigationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
    List<NavigationResult> findListBySpec(NavigationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-18
     */
     PageInfo<NavigationResult> findPageBySpec(NavigationParam param);

     void batchDelete(List<Long> ids);

}
