package cn.atsoft.dasheng.query.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.query.entity.QueryLog;
import cn.atsoft.dasheng.query.model.params.QueryLogParam;
import cn.atsoft.dasheng.query.model.result.QueryLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 搜索查询记录 服务类
 * </p>
 *
 * @author song
 * @since 2022-05-19
 */
public interface QueryLogService extends IService<QueryLog> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-05-19
     */
    void add(QueryLogParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-05-19
     */
    void delete(QueryLogParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-05-19
     */
    void update(QueryLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-05-19
     */
    QueryLogResult findBySpec(QueryLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-05-19
     */
    List<QueryLogResult> findListBySpec(QueryLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-05-19
     */
     PageInfo<QueryLogResult> findPageBySpec(QueryLogParam param);

}
