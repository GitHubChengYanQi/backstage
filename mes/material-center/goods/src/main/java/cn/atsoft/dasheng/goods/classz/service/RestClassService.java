package cn.atsoft.dasheng.goods.classz.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.model.params.RestClassParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 物品分类表 服务类
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
public interface RestClassService extends IService<RestClass> {


    /**
     * 新增
     *
     * @author jazz
     * @Date 2021-10-18
     */
    Long   add(RestClassParam param);

    /**
     * 删除
     *
     * @author jazz
     * @Date 2021-10-18
     */
    void delete(RestClassParam param);

    /**
     * 更新
     *
     * @author jazz
     * @Date 2021-10-18
     */
    void update(RestClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author jazz
     * @Date 2021-10-18
     */
    RestClassResult findBySpec(RestClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author jazz
     * @Date 2021-10-18
     */
    List<RestClassResult> findListBySpec(RestClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author jazz
     * @Date 2021-10-18
     */
     PageInfo<RestClassResult> findPageBySpec(RestClassParam param);

}
