package cn.atsoft.dasheng.goods.texture.service;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.texture.entity.RestTextrue;
import cn.atsoft.dasheng.goods.texture.model.params.RestTextrueParam;
import cn.atsoft.dasheng.goods.texture.model.result.RestTextrueResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 材质 服务类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface RestTextrueService extends IService<RestTextrue> {

    /**
     * 新增
     *
     * @author 1
     * @Date 2021-07-14
     */
    Long add(RestTextrueParam param);

    /**
     * 删除
     *
     * @author 1
     * @Date 2021-07-14
     */
    void delete(RestTextrueParam param);

    /**
     * 更新
     *
     * @author 1
     * @Date 2021-07-14
     */
    void update(RestTextrueParam param);

    List<RestTextrueResult> details(List<Long> materialIds);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    RestTextrueResult findBySpec(RestTextrueParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<RestTextrueResult> findListBySpec(RestTextrueParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 1
     * @Date 2021-07-14
     */
     PageInfo findPageBySpec(RestTextrueParam param, DataScope dataScope );

}
