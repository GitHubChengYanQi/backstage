package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.model.params.InstockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
public interface InstockLogDetailService extends IService<InstockLogDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    void add(InstockLogDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    void delete(InstockLogDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    void update(InstockLogDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    InstockLogDetailResult findBySpec(InstockLogDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    List<InstockLogDetailResult> findListBySpec(InstockLogDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
     PageInfo<InstockLogDetailResult> findPageBySpec(InstockLogDetailParam param);

    List<InstockLogDetailResult> resultsByLogIds(List<Long> logIds);
}
