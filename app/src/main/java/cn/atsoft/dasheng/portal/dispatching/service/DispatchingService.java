package cn.atsoft.dasheng.portal.dispatching.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.dispatching.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.model.result.DispatchingResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 派工表 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-23
 */
public interface DispatchingService extends IService<Dispatching> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-23
     */
    void add(DispatchingParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-23
     */
    void delete(DispatchingParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-23
     */
    void update(DispatchingParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-23
     */
    DispatchingResult findBySpec(DispatchingParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-23
     */
    List<DispatchingResult> findListBySpec(DispatchingParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-23
     */
     PageInfo<DispatchingResult> findPageBySpec(DispatchingParam param);

     void addwx (DispatchingParam param);

}
