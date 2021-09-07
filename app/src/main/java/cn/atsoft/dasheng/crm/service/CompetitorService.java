package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
public interface CompetitorService extends IService<Competitor> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-07
     */
    Competitor add(CompetitorParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-07
     */
    void delete(CompetitorParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-07
     */
    void update(CompetitorParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
    CompetitorResult findBySpec(CompetitorParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
    List<CompetitorResult> findListBySpec(CompetitorParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
     PageInfo<CompetitorResult> findPageBySpec(CompetitorParam param);

}
