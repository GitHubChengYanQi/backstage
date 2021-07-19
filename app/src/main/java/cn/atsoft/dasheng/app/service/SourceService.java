package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Source;
import cn.atsoft.dasheng.app.model.params.SourceParam;
import cn.atsoft.dasheng.app.model.result.SourceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 来源表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-19
 */
public interface SourceService extends IService<Source> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-19
     */
    void add(SourceParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-19
     */
    void delete(SourceParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-19
     */
    void update(SourceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-19
     */
    SourceResult findBySpec(SourceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-19
     */
    List<SourceResult> findListBySpec(SourceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-19
     */
     PageInfo<SourceResult> findPageBySpec(SourceParam param);

}
